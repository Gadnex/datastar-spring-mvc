@import gg.jte.support.LocalizationSupport

@param LocalizationSupport localizer

<ul>
    <li><a class="pico-background-azure-750" href="/">${localizer.localize("page.Home")}</a></li>
    <li><a class="pico-background-azure-750" href="/todos">${localizer.localize("page.Todos")}</a></li>
    <li><a class="pico-background-azure-750" href="/add-person">${localizer.localize("page.Person")}</a></li>
    <li>
        @template.counter.Connections()
    </li>
</ul>