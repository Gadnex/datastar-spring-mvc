@import gg.jte.support.LocalizationSupport
@import io.github.gadnex.datastarspringmvc.person.Person

@param LocalizationSupport localizer
@param Person person
@param boolean fragment

<div id="person">
    <h1>Person</h1>
    @if(person == null)
        <p class="pico-color-red-500">
            No person added yet
        </p>
    @else
        <p>
            <label>
                Name
                <input name="name" value="${person.name()}" disabled>
            </label>
        </p>
        <p>
            <label>
                Email
                <input type="email" name="email" value="${person.email()}" disabled>
            </label>
        </p>
        @if(!fragment)
            <p class="pico-color-green-500">
                Whole page loaded
            </p>
        @else
            <p class="pico-color-green-500">
                Only a fragment loaded
            </p>
        @endif
    @endif
</div>