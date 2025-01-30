package io.github.gadnex.datastarspringmvc.person;

import io.github.gadnex.jtedatastar.Datastar;
import io.github.gadnex.jtedatastar.EmitException;
import jakarta.validation.Valid;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Controller
@Slf4j
public class PersonController {

  private static Person PERSON;
  private static ExecutorService EXECUTOR = Executors.newCachedThreadPool();

  private final Datastar datastar;

  public PersonController(final Datastar datastar) {
    this.datastar = datastar;
  }

  @GetMapping("add-person")
  public String addPerson(Model model, @RequestParam(required = false) String datastar) {
    model.addAttribute("datastar", datastar);
    return "person/AddPersonPage";
  }

  @PostMapping("add-person")
  public String addPerson(@Valid Person person, BindingResult bindingResult, Model model) {
    if (bindingResult.hasErrors()) {
      model.addAttribute("result", bindingResult);
      return "person/AddPersonPage";
    }
    PersonController.PERSON = person;
    return "redirect:/person";
  }

  @PostMapping(value = "data-star/add-person")
  public SseEmitter addPersonDatastar(
      @ModelAttribute @Valid Person person,
      BindingResult bindingResult,
      Model model,
      @RequestParam(name = "datastar", required = false) String datastarType) {
    log.info("person: {}", person);
    log.info("datastar: {}", datastarType);
    SseEmitter sseEmitter = new SseEmitter();
    sseEmitter.onCompletion(
        () -> {
          log.info("SSE emitter completed");
        });
    EXECUTOR.execute(
        () -> {
          try {
            if (bindingResult.hasErrors()) {
              datastar
                  .mergeFragments(sseEmitter)
                  .template("person/AddPerson", LocaleContextHolder.getLocale())
                  .attribute("result", bindingResult)
                  .emit();
            } else {
              PersonController.PERSON = person;
              if (datastarType != null && datastarType.equals("redirect")) {
                log.info("ExecuteScript redirecting");
                datastar.executeScript(sseEmitter).script("window.location = \"/person\"").emit();
              } else {
                log.info("MergeFragments");
                datastar
                    .mergeFragments(sseEmitter)
                    .template("person/Person", LocaleContextHolder.getLocale())
                    .attribute("person", PersonController.PERSON)
                    .attribute("fragment", true)
                    .emit();
                datastar
                    .executeScript(sseEmitter)
                    .autoRemove(true)
                    .script("history.pushState({}, \"\", \"/person\")")
                    .emit();
                datastar
                    .executeScript(sseEmitter)
                    .script("window.onpopstate = function(event) { location.reload(); }")
                    .emit();
              }
            }
          } catch (EmitException ex) {
            log.error(ex.getMessage(), ex);
          } finally {
            sseEmitter.complete();
          }
        });
    return sseEmitter;
  }

  @GetMapping("person")
  public String person(Model model) {
    model.addAttribute("person", PersonController.PERSON);
    return "person/PersonPage";
  }
}
