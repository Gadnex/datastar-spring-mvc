@import gg.jte.support.LocalizationSupport

@param LocalizationSupport localizer

@template.layout.Layout(localizer = localizer, content = @`
    <div data-signals="{
        countTo: 1000
    }">
    </div>
    <label>
        ${localizer.localize("home.countTo")}
        <input data-bind="countTo">
    </label>
    <button type="submit"
            data-on-click="@post('/counter')"
            data-attr-disabled="$counting">
        Submit
    </button>
    <div data-on-load="@get('connect')"></div>
    <div id="counter"></div>
    <label>
        ${localizer.localize("home.debug")}
        <input type="checkbox" data-bind="debug">
    </label>
    <pre class="pico-background-grey-100"
         data-text="ctx.signals.JSON()"
         data-class-hidden="!$debug"></pre>
    <label>
        ${localizer.localize("home.logCompletion")}
        <input type="checkbox" data-bind="logCompletion">
    </label>
`)
