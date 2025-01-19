@import gg.jte.support.LocalizationSupport

@param LocalizationSupport localizer

<ul>
    <li><a class="pico-background-azure-750" href="/">${localizer.localize("page.Home")}</a></li>
    <li>
        @template.counter.Connections()
    </li>
</ul>