@import gg.jte.support.LocalizationSupport
@import static io.github.gadnex.datastarspringmvc.chaos.ChaosController.CHAOS_ENABLED

@param LocalizationSupport localizer

<ul>
    <li><a class="pico-background-azure-750" href="/">${localizer.localize("page.Home")}</a></li>
    <li><a class="pico-background-azure-750" href="/todos">${localizer.localize("page.Todos")}</a></li>
    @if(CHAOS_ENABLED)
        <li><a class="pico-background-azure-750" href="/chaos">${localizer.localize("page.Chaos")}</a></li>
    @endif
    <li><a class="pico-background-azure-750" href="/add-person">${localizer.localize("page.Person")}</a></li>
    <li>
        @template.counter.Connections()
    </li>
</ul>