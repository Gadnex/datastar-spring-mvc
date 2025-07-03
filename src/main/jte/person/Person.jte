@import gg.jte.support.LocalizationSupport
@import io.github.gadnex.datastarspringmvc.person.Person

@param LocalizationSupport localizer
@param Person person

@template.layout.Layout(localizer = localizer, content = @`
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
        @endif
    </div>
`)