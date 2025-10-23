@import gg.jte.support.LocalizationSupport

@param LocalizationSupport localizer

@template.layout.Layout(localizer = localizer, content = @`
    <div data-signals="{
        _counting: false,
        countTo: 1000
    }">
    </div>
    <label>
        ${localizer.localize("home.countTo")}
        <input data-bind="countTo">
    </label>
    <button type="submit"
            data-on:click="@post('/counter')"
            data-attr:disabled="$_counting">
        Submit
    </button>
    <div data-init="@get('connect')"></div>
    <div id="counter"></div>
    <label>
        ${localizer.localize("home.debug")}
        <input type="checkbox" data-bind="_debug">
    </label>
    <pre class="pico-background-grey-100"
         data-class:hidden="!$_debug"
         data-json-signals></pre>
    <label>
        ${localizer.localize("home.logCompletion")}
        <input type="checkbox" data-bind="logCompletion">
    </label>
`)
