@import static io.github.gadnex.datastarspringmvc.chaos.ChaosController.CHAOS_ENABLED

@if(CHAOS_ENABLED)
    <section id="soundBoard" class="grid m-b-4">
        <button class="primary"data-on:click="@get('/chaos/play/arcade-retro-game-over')">
            Arcade retro game over
        </button>
        <button class="primary"data-on:click="@get('/chaos/play/crowd-laugh')">
            Crowd laugh
        </button>
        <button class="primary"data-on:click="@get('/chaos/play/dog-barking-twice')">
            Dog barking twice
        </button>
        <button class="primary"data-on:click="@get('/chaos/play/fast-rocket-whoosh')">
            Fast rocket whoosh
        </button>
        <button class="primary"data-on:click="@get('/chaos/play/flock-of-wild-geese')">
            Flock of wild geese
        </button>
        <button class="primary"data-on:click="@get('/chaos/play/rain-and-thunder-storm')">
            Rain and thunder storm
        </button>
        <button class="primary"data-on:click="@get('/chaos/play/sad-game-over-trombone')">
            Sad game-over trombone
        </button>
        <button class="primary"data-on:click="@get('/chaos/play/sick-man-sneeze')">
            Sick man sneeze
        </button>
        <button class="primary"data-on:click="@get('/chaos/play/small-group-cheer-and-applause')">
            Small group cheer and applause
        </button>
        <button class="primary"data-on:click="@get('/chaos/play/stadium-crowd-light-applause')">
            Stadium crowd light applause
        </button>
        <button class="primary"data-on:click="@get('/chaos/play/transition-windy-swoosh')">
            Transition windy swoosh
        </button>
        <button class="primary"data-on:click="@get('/chaos/play/vintage-telephone-ringtone')">
            Vintage telephone ringtone
        </button>
    </section>
@else
    <section id="soundBoard" class="box m-b-4 bg-error text-4 text-center">
        The chaos master has spoken
    </section>
@endif