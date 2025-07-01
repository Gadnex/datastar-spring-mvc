package io.github.gadnex.datastarspringmvc;

import io.github.gadnex.jtedatastar.Datastar;
import io.github.gadnex.jtedatastar.PatchMode;
import io.micrometer.core.annotation.Timed;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Component
@Slf4j
public class CounterWorker {

  // The Datastar bean from the plugin is injected
  private final Datastar datastar;
  // Map to hold all the SSE emitters for the connected client
  // and their locales for rendering fragments in the
  // correct language for the emitter
  private final Map<SseEmitter, Locale> connections;
  // This object will be sent to the browser as signals
  private final Map<String, Object> foo;

  private long emitCounter;

  public CounterWorker(final Datastar Datastar) {
    this.datastar = Datastar;
    connections = new ConcurrentHashMap<>();

    foo = new HashMap<>();
    foo.put("int", 123);
    foo.put("long", 456L);
    foo.put("float", 789.12f);
    foo.put("double", 789.12d);
    foo.put("boolean", true);
    foo.put("string", "Hello World");
    Map<String, Object> bar = new HashMap<>();
    bar.put("nested", "I am nested");
    foo.put("bar", bar);
  }

  public void connect(SseEmitter sseEmitter, Locale locale) {
    addConnection(sseEmitter, locale);
  }

  // Note the @Async annotation
  @Async
  @Timed("datastar.count")
  public void count(CounterRequest counterRequest) {
    emitCounter = 0;
    long startTime = System.currentTimeMillis();
    disableSubmitButtonAndAddFooSignalOnAllConnectedClients();
    addProgressBarOnAllConnectedClients();
    addCountFragmentsAndSignalsOnAllConnectedClients(counterRequest);
    removeFooSignalFromAllConnectedClients();
    removeProgressBarFromAllConnectedClients();
    if (counterRequest.logCompletion()) {
      executeScriptOnAllConnectedClientsToLogCompletion();
    }
    long endTime = System.currentTimeMillis();
    calculatePerformanceStatisticsAndSendAsSignalToAllConnectedClients(
        counterRequest, startTime, endTime);
  }

  private void disableSubmitButtonAndAddFooSignalOnAllConnectedClients() {
    if (!connections.isEmpty()) {
      datastar
          .patchSignals(connections.keySet())
          .signal("_counting", true)
          .signal("_counter", 0)
          .signal("foo", foo)
          .emit();
      emitCounter += connections.size();
    }
  }

  private void addProgressBarOnAllConnectedClients() {
    if (!connections.isEmpty()) {
      datastar
          .patchElements(connections.keySet())
          .patchMode(PatchMode.BEFORE)
          .selector("#counter")
          .template("counter/ProgressBar")
          .emit();
      emitCounter += connections.size();
    }
  }

  private void addCountFragmentsAndSignalsOnAllConnectedClients(CounterRequest counterRequest) {
    Map<Locale, Set<SseEmitter>> connectionsByLocale = groupConnectionsByLocale();
    for (int i = 1; i <= counterRequest.countTo(); i++) {
      for (Locale localeFromConnections : connectionsByLocale.keySet()) {
        datastar
            .patchElements(connectionsByLocale.get(localeFromConnections))
            .template("counter/Counter", localeFromConnections)
            .attribute("counter", i)
            .emit();
        emitCounter = emitCounter + connectionsByLocale.get(localeFromConnections).size();
      }
      if (!connections.isEmpty()) {
        datastar.patchSignals(connections.keySet()).signal("_counter", i).emit();
        emitCounter += connections.size();
      }
      //      try {
      //        Thread.sleep(1000);
      //      } catch (InterruptedException e) {
      //        throw new RuntimeException(e);
      //      }
    }
  }

  private void removeFooSignalFromAllConnectedClients() {
    if (!connections.isEmpty()) {
      datastar.patchSignals(connections.keySet()).signal("foo", null).emit();
      emitCounter += connections.size();
    }
  }

  private void removeProgressBarFromAllConnectedClients() {
    if (!connections.isEmpty()) {
      datastar
          .patchElements(connections.keySet())
          .patchMode(PatchMode.REMOVE)
          .selector("#progress")
          .emit();
      emitCounter += connections.size();
    }
  }

  private void executeScriptOnAllConnectedClientsToLogCompletion() {
    if (!connections.isEmpty()) {
      datastar
          .executeScript(connections.keySet())
          .script("console.log('Counter completed')")
          .emit();
      emitCounter += connections.size();
    }
  }

  private void calculatePerformanceStatisticsAndSendAsSignalToAllConnectedClients(
      CounterRequest counterRequest, long startTime, long endTime) {
    if (!connections.isEmpty()) {
      long emitTimeMillis = endTime - startTime;
      float emitsPerMillisecond = (float) emitCounter / emitTimeMillis;
      int emitsPerSecond = (int) (emitsPerMillisecond * 1000);
      int refreshRateHzPerConnection = emitsPerSecond / connections.size();
      int maxConcurrentClientsAt60Hz = emitsPerSecond / 60;
      datastar
          .patchSignals(connections.keySet())
          .signal("_counting", false)
          .signal("_emitTimeMillis", emitTimeMillis)
          .signal("_emits", emitCounter)
          .signal("_emitsPerMillisecond", emitsPerMillisecond)
          .signal("_emitsPerSecond", emitsPerSecond)
          .signal("_refreshRateHzPerConnection", refreshRateHzPerConnection)
          .signal("_maxConcurrentClientsAt60Hz", maxConcurrentClientsAt60Hz)
          .emit();
      log.atInfo()
          .addKeyValue("emits", emitCounter)
          .addKeyValue("emitTimeMillis", emitTimeMillis)
          .addKeyValue("emitsPerSecond", emitsPerSecond)
          .log("Counter completed");
    }
  }

  private Map<Locale, Set<SseEmitter>> groupConnectionsByLocale() {
    Map<Locale, Set<SseEmitter>> connectionsByLocale = new ConcurrentHashMap<>();
    for (Map.Entry<SseEmitter, Locale> entry : connections.entrySet()) {
      Set<SseEmitter> sseEmitters = connectionsByLocale.get(entry.getValue());
      if (sseEmitters == null) {
        sseEmitters = new HashSet<>();
      }
      sseEmitters.add(entry.getKey());
      connectionsByLocale.put(entry.getValue(), sseEmitters);
    }
    return connectionsByLocale;
  }

  private void addConnection(SseEmitter sseEmitter, Locale locale) {
    connections.put(sseEmitter, locale);
    sseEmitter.onCompletion(
        () -> {
          connections.remove(sseEmitter);
          emitConnectionCount();
        });
    sseEmitter.onError(
        (error) -> {
          connections.remove(sseEmitter);
          emitConnectionCount();
        });
    sseEmitter.onTimeout(
        () -> {
          connections.remove(sseEmitter);
          emitConnectionCount();
        });
    emitConnectionCount();
  }

  private void emitConnectionCount() {
    if (!connections.isEmpty()) {
      datastar
          .patchElements(connections.keySet())
          .template("counter/Connections")
          .attribute("connectionCount", connections.size())
          .emit();
    }
  }
}
