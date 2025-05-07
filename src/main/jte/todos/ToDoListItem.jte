@import io.github.gadnex.datastarspringmvc.todos.ToDo

@param ToDo todo

<article class="todo" id="todo-${todo.id().toString()}">
    <div>${todo.text()}</div>
    <div>
        @if(todo.done())
            <button class="material-icons" data-on-click="@put('/todos/${todo.id().toString()}/')">check_box</button>
        @else
            <button class="material-icons" data-on-click="@put('/todos/${todo.id().toString()}/')">check_box_outline_blank</button>
        @endif
    </div>
</article>