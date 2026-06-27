@import io.github.gadnex.datastarspringmvc.todos.ToDo

@param ToDo todo

<article id="todo-${todo.id().toString()}" class="cluster cross-center bg-primary-2 p-4 m-b-3">
    <span class="todo-text">${todo.text()}</span>
    <div class="todo-buttons cluster main-end">
        @if(todo.done())
            <button class="ghost text-primary" data-on:click="@put('/todos/${todo.id().toString()}/')">
                <svg xmlns="http://www.w3.org/2000/svg" class="icon" width="1em" height="1em" viewBox="0 0 512 512">
                    <path d="M0 0h512v512H0z" fill="none"/>
                    <path fill="none" stroke="currentColor" stroke-linecap="round" stroke-linejoin="round"
                          stroke-width="32" d="M352 176L217.6 336L160 272"/>
                    <rect width="384" height="384" x="64" y="64" fill="none" stroke="currentColor"
                          stroke-linejoin="round" stroke-width="32" rx="48" ry="48"/>
                </svg>
            </button>
        @else
            <button class="ghost text-primary" data-on:click="@put('/todos/${todo.id().toString()}/')">
                <svg xmlns="http://www.w3.org/2000/svg" class="icon" width="1em" height="1em" viewBox="0 0 512 512">
                    <path d="M0 0h512v512H0z" fill="none"/>
                    <rect width="384" height="384" x="64" y="64" fill="none" stroke="currentColor"
                          stroke-linejoin="round" stroke-width="32" rx="48" ry="48"/>
                </svg>
            </button>
        @endif
        <button class="ghost text-primary" data-on:click="@delete('/todos/${todo.id().toString()}/')">
            <svg xmlns="http://www.w3.org/2000/svg" class="icon" width="1em" height="1em" viewBox="0 0 24 24">
                <path d="M0 0h24v24H0z" fill="none"/>
                <path fill="currentColor"
                      d="M7 21q-.825 0-1.412-.587T5 19V6H4V4h5V3h6v1h5v2h-1v13q0 .825-.587 1.413T17 21zM17 6H7v13h10zM9 17h2V8H9zm4 0h2V8h-2zM7 6v13z"/>
            </svg>
        </button>
    </div>
</article>