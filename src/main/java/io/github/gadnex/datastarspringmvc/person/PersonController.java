package io.github.gadnex.datastarspringmvc.person;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@Slf4j
public class PersonController {

  private static Person person;

  @GetMapping("add-person")
  public String addPerson(Model model) {
    return "person/AddPerson";
  }

  @PostMapping("add-person")
  public String addPerson(@Valid Person person, BindingResult bindingResult, Model model) {
    if (bindingResult.hasErrors()) {
      model.addAttribute("person", person);
      model.addAttribute("result", bindingResult);
      return "person/AddPerson";
    }
    PersonController.person = person;
    return "redirect:/person";
  }

  @GetMapping("person")
  public String person(Model model) {
    model.addAttribute("person", PersonController.person);
    return "person/Person";
  }
}
