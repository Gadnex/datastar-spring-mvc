@import static io.github.gadnex.datastarspringmvc.chaos.ChaosController.CHAOS_ENABLED

@if(CHAOS_ENABLED)
    <section id="soundBoard">
        <button data-on-click="@get('/chaos/play/arcade-retro-game-over')">
            Arcade retro game over
        </button>
        <button data-on-click="@get('/chaos/play/crowd-laugh')">
            Crowd laugh
        </button>
        <button data-on-click="@get('/chaos/play/dog-barking-twice')">
            Dog barking twice
        </button>
        <button data-on-click="@get('/chaos/play/fast-rocket-whoosh')">
            Fast rocket whoosh
        </button>
        <button data-on-click="@get('/chaos/play/flock-of-wild-geese')">
            Flock of wild geese
        </button>
        <button data-on-click="@get('/chaos/play/rain-and-thunder-storm')">
            Rain and thunder storm
        </button>
        <button data-on-click="@get('/chaos/play/sad-game-over-trombone')">
            Sad game-over trombone
        </button>
        <button data-on-click="@get('/chaos/play/sick-man-sneeze')">
            Sick man sneeze
        </button>
        <button data-on-click="@get('/chaos/play/small-group-cheer-and-applause')">
            Small group cheer and applause
        </button>
        <button data-on-click="@get('/chaos/play/stadium-crowd-light-applause')">
            Stadium crowd light applause
        </button>
        <button data-on-click="@get('/chaos/play/transition-windy-swoosh')">
            Transition windy swoosh
        </button>
        <button data-on-click="@get('/chaos/play/vintage-telephone-ringtone')">
            Vintage telephone ringtone
        </button>
    </section>
@else
    <section id="soundBoard">
        The chaos master has spoken
    </section>
@endif