@import gg.jte.support.LocalizationSupport
@import io.github.gadnex.datastarspringmvc.todos.ToDo
@import org.springframework.validation.BindingResult
@import java.util.Collection

@param LocalizationSupport localizer
@param BindingResult result
@param Collection<ToDo> todos

@template.layout.Layout(localizer = localizer, content = @`
    <h2>${localizer.localize("page.Todos")}</h2>
    <div data-init="@get('/todos/connect')"></div>
    @template.todos.AddToDoForm(localizer = localizer, result = result)
    <div id="todo-list">
        @for(ToDo todo : todos)
            @template.todos.ToDoListItem(todo = todo)
        @endfor
    </div>
`)