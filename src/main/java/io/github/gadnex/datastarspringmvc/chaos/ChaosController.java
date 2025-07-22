package io.github.gadnex.datastarspringmvc.chaos;

import io.github.gadnex.jtedatastar.Datastar;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Controller
@RequestMapping("chaos")
@RequiredArgsConstructor
public class ChaosController {

  private static final ExecutorService EXECUTOR = Executors.newVirtualThreadPerTaskExecutor();

  private final Datastar datastar;

  public static boolean CHAOS_ENABLED = false;
  private final Set<SseEmitter> players = new HashSet<>();
  private AtomicLong chaosCounter = new AtomicLong();

  @GetMapping
  public String chaos(Model model) {
    if (!CHAOS_ENABLED) {
      return "redirect:/";
    }
    model.addAttribute("playerCount", players.size());
    model.addAttribute("chaosCounter", chaosCounter.get());
    return "chaos/Chaos";
  }

  @GetMapping("on")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void on() {
    CHAOS_ENABLED = true;
    chaosCounter = new AtomicLong();
    EXECUTOR.execute(
        () -> {
          if (!players.isEmpty()) {
            datastar.patchElements(players).template("chaos/SoundBoard").emit();
          }
        });
  }

  @GetMapping("off")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void off() {
    CHAOS_ENABLED = false;
    EXECUTOR.execute(
        () -> {
          if (!players.isEmpty()) {
            datastar.patchElements(players).template("chaos/SoundBoard").emit();
            datastar.executeScript(players).autoRemove(true).script("sound.stop()").emit();
          }
        });
  }

  @GetMapping("connect")
  public SseEmitter connect(Model model) {
    if (!CHAOS_ENABLED) {
      return null;
    }
    SseEmitter sseEmitter = new SseEmitter(-1L);
    EXECUTOR.execute(
        () -> {
          players.add(sseEmitter);
          sseEmitter.onCompletion(
              () -> {
                players.remove(sseEmitter);
                updateChaosMeter();
              });
          sseEmitter.onError(
              (error) -> {
                players.remove(sseEmitter);
                updateChaosMeter();
              });
          sseEmitter.onTimeout(
              () -> {
                players.remove(sseEmitter);
                updateChaosMeter();
              });
          updateChaosMeter();
        });

    return sseEmitter;
  }

  @GetMapping("/play/{sprite}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void play(@PathVariable(name = "sprite") String sprite, Model model) {
    if (!CHAOS_ENABLED) {
      return;
    }
    EXECUTOR.execute(
        () -> {
          chaosCounter.incrementAndGet();
          if (!players.isEmpty()) {
            datastar
                .executeScript(players)
                .autoRemove(true)
                .script("sound.play('" + sprite + "')")
                .emit();
            updateChaosMeter();
          }
        });
  }

  private void updateChaosMeter() {
    if (!players.isEmpty()) {
      datastar
          .patchElements(players)
          .template("chaos/ChaosMeter")
          .attribute("playerCount", players.size())
          .attribute("chaosCounter", chaosCounter.get())
          .emit();
    }
  }
}
