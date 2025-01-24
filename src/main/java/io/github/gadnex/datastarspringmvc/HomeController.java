package io.github.gadnex.datastarspringmvc;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Controller
@AllArgsConstructor
@Slf4j
public class HomeController {

  private CounterWorker counterWorker;

  @GetMapping
  public String home() {
    // A standard Spring MVC response to render the home page to the browser.
    return "Home";
  }

  @GetMapping("connect")
  public SseEmitter connect() {
    // We want to create a new SSE, pass it to the worker that
    // does the real work asynchronously and the return the
    // SSE emitter as quickly as possible.
    // The timeout value -1L will keep the connection open indefinitely.
    SseEmitter sseEmitter = new SseEmitter(-1L);
    counterWorker.connect(sseEmitter, LocaleContextHolder.getLocale());
    return sseEmitter;
  }

  @PostMapping("counter")
  @ResponseStatus(HttpStatus.OK)
  public void counter(@RequestBody CounterRequest counterRequest) {
    // We already have an open SSE emitter from the method above that
    // will stream the results to the browser.
    // The worker is called to do the real work asynchronously.
    counterWorker.count(counterRequest);
  }

  @GetMapping("error")
  public String error() {
    return "Error";
  }
}
