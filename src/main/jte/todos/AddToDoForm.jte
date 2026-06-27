@import gg.jte.support.LocalizationSupport
@import org.springframework.validation.BindingResult
@import java.util.UUID
@import org.springframework.validation.FieldError

@param LocalizationSupport localizer
@param BindingResult result

<section id="add-todo-form">
    <form action="/todos"
          method="post"
          data-on:submit="@post('/todos', {contentType: 'form'})"
          data-signals:_todo-text="''"
          class="box stack">
        <input type="hidden" name="id" value="${UUID.randomUUID().toString()}">
        <input type="hidden" name="done" value="false">
        <div>
            <label for="new-todo">${localizer.localize("Todos.text")}</label>
            @if(result == null)
                <div>
                    <input name="text" id="new-todo" class="w-full" data-bind:_todo-text autofocus>
                </div>
            @else
                @if(result.getFieldError("text") == null)
                    <div>
                        <input id="new-todo" name="text" data-bind:_todo-text class="w-full">
                    </div>
                @else
                    <div>
                        <input id="new-todo"
                               name="text"
                               aria-invalid="true"
                               value="${result.getFieldError("text").getRejectedValue().toString()}"
                               class="w-full">
                        <ul class="m-i-0 m-b-2">
                            @for(FieldError fieldError : result.getFieldErrors("text"))
                                <li><small>${fieldError.getDefaultMessage()}</small></li>
                            @endfor
                        </ul>
                    </div>
                @endif
            @endif
        </div>
        <div class="cluster main-end">
            <button type="submit" class="primary">Add</button>
        </div>
    </form>
</section>