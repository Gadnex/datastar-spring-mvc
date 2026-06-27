@import gg.jte.support.LocalizationSupport

@param LocalizationSupport localizer
@param int counter

<div id="counter">
    <div>${localizer.localize("counter")}</div>
    <div class="frame bg-primary-5 p-4 stack main-center cross-center">
        <span class="counter">${counter}</span>
    </div>
</div>