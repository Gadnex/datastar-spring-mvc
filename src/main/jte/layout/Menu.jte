@import gg.jte.support.LocalizationSupport

@param LocalizationSupport localizer

<ul>
    <li><a class="pico-background-azure-750" href="/">${localizer.localize("page.Home")}</a></li>
    <li><a class="pico-background-azure-750" href="/todos">${localizer.localize("page.Todos")}</a></li>
    <li>
        <details class="dropdown">
            <summary>
                ${localizer.localize("page.Person")}
            </summary>
            <ul dir="rtl">
                <li><a href="/add-person">No Datastar</a></li>
                <li><a href="/add-person?datastar=redirect">Datastar Redirect</a></li>
                <li><a href="/add-person?datastar=push-url">Datastar Push URL</a></li>
            </ul>
        </details>
    </li>
    <li>
        @template.counter.Connections()
    </li>
</ul>