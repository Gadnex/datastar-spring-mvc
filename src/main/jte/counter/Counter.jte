@import gg.jte.support.LocalizationSupport

@param LocalizationSupport localizer
@param int counter

<div id="counter">
    <label>
        ${localizer.localize("counter")}
        <article>
            ${counter}
        </article>
    </label>
</div>