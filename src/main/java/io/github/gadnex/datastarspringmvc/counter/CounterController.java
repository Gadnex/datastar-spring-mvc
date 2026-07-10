package io.github.gadnex.datastarspringmvc.counter;

import io.github.gadnex.jtedatastar.Datastar;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Controller
public class CounterController {

  private static final Logger log = LoggerFactory.getLogger(CounterController.class);

  private final CounterWorker counterWorker;

  public CounterController(CounterWorker counterWorker) {
    this.counterWorker = counterWorker;
  }

  @GetMapping
  public String home() {
    return "counter/CounterPage";
  }

  @GetMapping("/connect")
  public SseEmitter connect() {
    // We want to create a new SSE, pass it to the worker that
    // does the real work asynchronously and the return the
    // SSE emitter as quickly as possible.
    // The timeout value -1L will keep the connection open indefinitely.
    SseEmitter sseEmitter = new SseEmitter(-1L);
    counterWorker.connect(sseEmitter, LocaleContextHolder.getLocale());
    return sseEmitter;
  }

  @PostMapping(value = "/counter", headers = Datastar.REQUEST_HEADER)
  @ResponseStatus(HttpStatus.OK)
  public void counter(@RequestBody CounterRequest counterRequest) {
    if ((counterRequest.countTo() <= 0) || (counterRequest.countTo() > 10_000)) {
      return;
    }
    // We already have an open SSE emitter from the method above that
    // will stream the results to the browser.
    // The worker is called to do the real work asynchronously.
    counterWorker.count(counterRequest);
  }

  @PostMapping(
      value = "/counter/validate",
      headers = Datastar.REQUEST_HEADER,
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public Map<String, Object> counterValidate(@RequestBody CounterRequest counterRequest) {
    if ((counterRequest.countTo() <= 0) || (counterRequest.countTo() > 10_000)) {
      return Map.of("_invalidCountTo", true);
    } else {
      return Map.of("_invalidCountTo", false);
    }
  }
}
