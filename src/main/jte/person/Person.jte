@import gg.jte.support.LocalizationSupport
@import io.github.gadnex.datastarspringmvc.person.Person

@param LocalizationSupport localizer
@param Person person

@template.layout.Layout(localizer = localizer, currentPath = "/person", content = @`
    <div class="stack">
        <h2 class="text-3">Add person</h2>
        @if(person == null)
            <p class="">
                No person added yet
            </p>
        @else
            <div class="stack">
                <div>
                    <div>
                        <label for="name">${localizer.localize("Person.field.name")}</label>
                    </div>
                    <div>
                        <input id="name" name="name" value="${person.name()}" disabled>
                    </div>
                </div>
                <div>
                    <div>
                        <label for="email">${localizer.localize("Person.field.email")}</label>
                    </div>
                    <div>
                        <input type="email" id="email" name="email" value="${person.email()}" disabled>
                    </div>
                </div>
            </div>
        @endif
    </div>
`)