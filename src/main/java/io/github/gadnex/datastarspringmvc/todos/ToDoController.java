package io.github.gadnex.datastarspringmvc.todos;

import io.github.gadnex.jtedatastar.Datastar;
import io.github.gadnex.jtedatastar.PatchMode;
import jakarta.validation.Valid;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Controller
@RequestMapping("todos")
@RequiredArgsConstructor
@Slf4j
public class ToDoController {

  private static final Map<UUID, ToDo> TODOS = new HashMap<>();

  private static final ExecutorService EXECUTOR = Executors.newVirtualThreadPerTaskExecutor();

  private static final Set<SseEmitter> connections = new HashSet<>();

  private final Datastar datastar;
  private final View error;

  @GetMapping
  public String todos(Model model) {
    model.addAttribute("todos", TODOS.values());
    return "todos/ToDoList";
  }

  @GetMapping(value = "connect", headers = "Datastar-Request")
  public SseEmitter connect() {
    SseEmitter sseEmitter = new SseEmitter(-1L);
    sseEmitter.onError(
        (error) -> {
          connections.remove(sseEmitter);
        });
    sseEmitter.onCompletion(
        () -> {
          connections.remove(sseEmitter);
        });
    sseEmitter.onTimeout(
        () -> {
          connections.remove(sseEmitter);
        });
    connections.add(sseEmitter);
    return sseEmitter;
  }

  @PostMapping
  public String addTodo(
      @ModelAttribute @Valid ToDo todo, BindingResult bindingResult, Model model) {
    if (bindingResult.hasErrors()) {
      model.addAttribute("result", bindingResult);
      model.addAttribute("todos", TODOS.values());
      return "todos/ToDoList";
    }
    TODOS.put(todo.id(), todo);
    addToDoItemToAllClients(todo);
    return "redirect:/todos";
  }

  @PostMapping(headers = "Datastar-Request")
  public SseEmitter addTodoDatastar(
      @ModelAttribute @Valid ToDo todo, BindingResult bindingResult, Model model) {
    SseEmitter sseEmitter = new SseEmitter();
    EXECUTOR.execute(
        () -> {
          reloadAddToDoForm(bindingResult, sseEmitter);
          if (!bindingResult.hasErrors()) {
            TODOS.put(todo.id(), todo);
            addToDoItemToAllClients(todo);
          }
        });
    return sseEmitter;
  }

  private void reloadAddToDoForm(BindingResult bindingResult, SseEmitter sseEmitter) {
    datastar
        .patchElements(sseEmitter)
        .template("todos/AddToDoForm", LocaleContextHolder.getLocale())
        .attribute("result", bindingResult)
        .emit();
    sseEmitter.complete();
  }

  private void addToDoItemToAllClients(ToDo todo) {
    datastar
        .patchElements(connections)
        .template("todos/ToDoListItem")
        .attribute("todo", todo)
        .selector("#todo-list")
        .patchMode(PatchMode.PREPEND)
        .emit();
  }

  @PutMapping(value = "/{id}/", headers = "Datastar-Request")
  @ResponseStatus(HttpStatus.OK)
  public void toggleDone(@PathVariable(name = "id") UUID id) {
    EXECUTOR.execute(
        () -> {
          ToDo todo = TODOS.get(id);
          if (todo == null) {
            log.atError().addKeyValue("id", id).log("ToDo with id not found");
          } else {
            ToDo updatedTodo = new ToDo(todo.id(), todo.text(), !todo.done());
            TODOS.put(id, updatedTodo);
            datastar
                .patchElements(connections)
                .template("todos/ToDoListItem")
                .attribute("todo", updatedTodo)
                .emit();
          }
        });
  }

  @DeleteMapping(value = "/{id}/", headers = "Datastar-Request")
  @ResponseStatus(HttpStatus.OK)
  public void deleteTodo(@PathVariable(name = "id") UUID id) {
    EXECUTOR.execute(
        () -> {
          ToDo todo = TODOS.get(id);
          if (todo == null) {
            log.atError().addKeyValue("id", id).log("ToDo with id not found");
          } else {
            TODOS.remove(id, todo);
            datastar.removeFragments(connections).selector("#todo-" + id.toString()).emit();
          }
        });
  }

  private void removeConnections(Set<SseEmitter> sseEmitters) {
    for (SseEmitter sseEmitter : sseEmitters) {
      connections.remove(sseEmitter);
    }
  }
}
