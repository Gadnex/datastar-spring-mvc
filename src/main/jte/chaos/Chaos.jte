@import gg.jte.support.LocalizationSupport

@param String nonce
@param LocalizationSupport localizer
@param int playerCount
@param long chaosCounter

@template.layout.Layout(nonce = nonce, localizer = localizer, currentPath = "/chaos", content = @`
    <div class="stack">
        <h2 class="text-3">${localizer.localize("page.Chaos")}</h2>
        <p>Chaos is a minigame played by players in the same room on their mobile devices.</p>
        <p>The goal of the game is to create some chaos in the room.</p>

        <div data-init="@get('/chaos/connect')"></div>

        @template.chaos.ChaosMeter(playerCount = playerCount, chaosCounter = chaosCounter)

        @template.chaos.SoundBoard()

        <script src="/webjars/howler/dist/howler.core.min.js"></script>
        <script src="/js/chaos.js"></script>
    </div>
`)