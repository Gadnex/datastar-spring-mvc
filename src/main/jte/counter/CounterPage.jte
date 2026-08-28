@import gg.jte.support.LocalizationSupport

@param LocalizationSupport localizer

@template.layout.Layout(localizer = localizer, currentPath = "/", content = @`
    <div class="stack">
        <div class="cluster main-space-between cross-baseline">
            <h2 class="text-layout text-3" data-signals="{
                    _counting: false,
                    countTo: 1000
                }">
                ${localizer.localize("page.Counter")}
            </h2>
            <div>
                @template.counter.Connections()
            </div>
        </div>
        <div class="box stack">
            <div>
                <div>
                    <label for="count-to">${localizer.localize("counter.countTo")}</label>
                </div>
                <div>
                    <input type="number" min="1" max="10000" id="count-to"
                           data-bind="countTo"
                           data-on:keydown__debounce.500ms="@post('/counter/validate')"
                           data-attr:aria-invalid="$_invalidCountTo + ''"
                           class="w-full" autofocus>
                    <ul data-signals:_invalid-count-to="false"
                        data-show="$_invalidCountTo"
                        style="display: none;">
                        <li>Must be greater than 0</li>
                        <li>Must be less than or equal to 10 000</li>
                    </ul>
                </div>
            </div>
            <div class="cluster main-end">
                <button type="submit"
                        class="primary"
                        data-on:click="@post('/counter')"
                        data-attr:disabled="$_counting || $_invalidCountTo">
                    Submit
                </button>
            </div>
        </div>
        <div data-init="@get('connect')"></div>
        <div id="counter"></div>
        <div class="stack gap-2">
            <div>
                <input type="checkbox" id="debug" data-bind="_debug">
                <label for="debug">${localizer.localize("counter.debug")}</label>
            </div>
            <pre class="bg-neutral-3 p-2"
                 data-class:hidden="!$_debug"
                 data-json-signals></pre>
            <div>
                <input type="checkbox" id="log-completion" data-bind="logCompletion">
                <label for="log-completion">${localizer.localize("counter.logCompletion")}</label>
            </div>
        </div>
    </div>
`)
