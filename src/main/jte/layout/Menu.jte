@import gg.jte.support.LocalizationSupport
@import static io.github.gadnex.datastarspringmvc.chaos.ChaosController.CHAOS_ENABLED

@param LocalizationSupport localizer
@param String currentPath

<menu class="mobile-grow cluster main-center">
    <li><a href="/" aria-current="${"/".equals(currentPath) ? "page" : null}">${localizer.localize("page.Counter")}</a></li>
    <li><a href="/todos" aria-current="${"/todos".equals(currentPath) ? "page" : null}">${localizer.localize("page.Todos")}</a></li>
@if(CHAOS_ENABLED)
    <li><a href="/chaos" aria-current="${"/chaos".equals(currentPath) ? "page" : null}">${localizer.localize("page.Chaos")}</a></li>
@endif
    <li><a href="/add-person" aria-current="${"/add-person".equals(currentPath) ? "page" : null}">${localizer.localize("page.Person")}</a></li>
</menu>