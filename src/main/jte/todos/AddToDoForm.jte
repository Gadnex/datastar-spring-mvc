@import gg.jte.support.LocalizationSupport
@import org.springframework.validation.BindingResult
@import java.util.UUID

@param LocalizationSupport localizer
@param BindingResult result

<section id="add-todo-form">
    <form action="/todos"
          method="post"
          data-on-submit="@post('/todos', {contentType: 'form'})">
        <input type="hidden" name="id" value="${UUID.randomUUID().toString()}">
        <input type="hidden" name="done" value="false">
        @if(result == null)
            <label>
                ${localizer.localize("Todos.text")}
                <input id="new-todo-text" name="text" autofocus>
            </label>
        @else
            @if(result.getFieldError("text") == null)
                <label>
                    ${localizer.localize("Todos.text")}
                    <input  id="new-todo-text" name="text" autofocus>
                </label>
            @else
                <label>
                    ${localizer.localize("Todos.text")}
                    <input  id="new-todo-text" name="text" aria-invalid="true" autofocus>
                    <small>${result.getFieldError("text").getDefaultMessage()}</small>
                </label>
            @endif
        @endif
    </form>
</section>