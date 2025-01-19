package io.github.gadnex.datastarspringmvc;

import io.github.gadnex.jtedatastar.Datastar;
import io.github.gadnex.jtedatastar.EmitException;
import io.github.gadnex.jtedatastar.MergeMode;
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

  public CounterWorker(Datastar Datastar) {
    this.datastar = Datastar;
    connections = new ConcurrentHashMap<>();

    foo = new HashMap<>();
    foo.put("int", 123);
    foo.put("long", 456L);
    foo.put("float", 789.0f);
    foo.put("double", 789.0d);
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
    try {
      datastar
          .mergeSignals(connections.keySet())
          .signal("counting", true)
          .signal("counter", 0)
          .signal("foo", foo)
          .emit();
      emitCounter += connections.size();
    } catch (EmitException ex) {
      removeConnection(ex.emitters());
    }
  }

  private void addProgressBarOnAllConnectedClients() {
    try {
      datastar
          .mergeFragments(connections.keySet())
          .mergeMode(MergeMode.BEFORE)
          .selector("#counter")
          .template("counter/ProgressBar")
          .emit();
      emitCounter += connections.size();
    } catch (EmitException ex) {
      removeConnection(ex.emitters());
    }
  }

  private void addCountFragmentsAndSignalsOnAllConnectedClients(CounterRequest counterRequest) {
    Map<Locale, Set<SseEmitter>> connectionsByLocale = groupConnectionsByLocale();
    for (int i = 1; i <= counterRequest.countTo(); i++) {
      for (Locale localeFromConnections : connectionsByLocale.keySet()) {
        try {
          datastar
              .mergeFragments(connectionsByLocale.get(localeFromConnections), localeFromConnections)
              .settleDuration(500)
              .useViewTransition(true)
              .template("counter/Counter")
              .attribute("counter", i)
              .emit();
          emitCounter = emitCounter + connectionsByLocale.get(localeFromConnections).size();
          Thread.sleep(1);
        } catch (EmitException ex) {
          removeConnection(ex.emitters());
        } catch (InterruptedException ex) {
          log.warn("Sleep thread was interrupted", ex);
        }
      }
      try {
        datastar.mergeSignals(connections.keySet()).signal("counter", i).emit();
        emitCounter += connections.size();
      } catch (EmitException ex) {
        removeConnection(ex.emitters());
      }
    }
  }

  private void removeFooSignalFromAllConnectedClients() {
    try {
      datastar.removeSignals(connections.keySet()).path("foo").emit();
      emitCounter += connections.size();
    } catch (EmitException ex) {
      removeConnection(ex.emitters());
    }
  }

  private void removeProgressBarFromAllConnectedClients() {
    try {
      datastar.removeFragments(connections.keySet()).selector("#progress").emit();
      emitCounter += connections.size();
    } catch (EmitException ex) {
      removeConnection(ex.emitters());
    }
  }

  private void executeScriptOnAllConnectedClientsToLogCompletion() {
    try {
      datastar
          .executeScript(connections.keySet())
          .script("console.log('Counter completed')")
          .emit();
      emitCounter += connections.size();
    } catch (EmitException ex) {
      removeConnection(ex.emitters());
    }
  }

  private void calculatePerformanceStatisticsAndSendAsSignalToAllConnectedClients(
      CounterRequest counterRequest, long startTime, long endTime) {
    long emitTimeMillis = endTime - startTime;
    float emitsPerMillisecond = (float) emitCounter / emitTimeMillis;
    int emitsPerSecond = (int) (emitsPerMillisecond * 1000);
    int refreshRateHzPerConnection = emitsPerSecond / connections.size();
    int maxConcurrentClientsAt60Hz = emitsPerSecond / 60;
    try {
      datastar
          .mergeSignals(connections.keySet())
          .signal("counting", false)
          .signal("emitTimeMillis", emitTimeMillis)
          .signal("emits", emitCounter)
          .signal("emitsPerMillisecond", emitsPerMillisecond)
          .signal("emitsPerSecond", emitsPerSecond)
          .signal("refreshRateHzPerConnection", refreshRateHzPerConnection)
          .signal("maxConcurrentClientsAt60Hz", maxConcurrentClientsAt60Hz)
          .emit();
      emitCounter += connections.size();
    } catch (EmitException ex) {
      removeConnection(ex.emitters());
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

  private void removeConnection(Set<SseEmitter> sseEmitters) {
    for (SseEmitter sseEmitter : sseEmitters) {
      connections.remove(sseEmitter);
    }
    emitConnectionCount();
  }

  private void emitConnectionCount() {
    try {
      datastar
          .mergeFragments(connections.keySet())
          .template("counter/Connections")
          .attribute("connectionCount", connections.size())
          .emit();
    } catch (EmitException ex) {
      removeConnection(ex.emitters());
    }
  }
}
