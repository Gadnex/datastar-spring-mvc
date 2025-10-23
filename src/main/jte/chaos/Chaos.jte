@import gg.jte.support.LocalizationSupport

@param LocalizationSupport localizer
@param int playerCount
@param long chaosCounter

@template.layout.Layout(localizer = localizer, content = @`
    <h2>${localizer.localize("page.Chaos")}</h2>
    <p>Chaos is a minigame played by players in the same room on their mobile devices.</p>
    <p>The goal of the game is to create some chaos in the room.</p>

    <div data-init="@get('/chaos/connect')"></div>

    @template.chaos.ChaosMeter(playerCount = playerCount, chaosCounter = chaosCounter)

    @template.chaos.SoundBoard()

    <script src="/webjars/howler/dist/howler.core.min.js"></script>
    <script>
        var sound = new Howl({
            src: ['/chaos/chaos.mp3', '/chaos/chaos.ogg'],
            sprite: {
                "arcade-retro-game-over": [0, 1667],
                "crowd-laugh": [3000, 4125],
                "dog-barking-twice": [9000, 1623],
                "fast-rocket-whoosh": [12000, 4747],
                "flock-of-wild-geese": [18000, 5218],
                "rain-and-thunder-storm": [25000, 29000],
                "sad-game-over-trombone": [55000, 5613],
                "sick-man-sneeze": [62000, 861],
                "small-group-cheer-and-applause": [64000, 10597],
                "stadium-crowd-light-applause": [76000, 11093],
                "transition-windy-swoosh": [89000, 3203],
                "vintage-telephone-ringtone": [94000, 7387]
            }
        });
    </script>
`)