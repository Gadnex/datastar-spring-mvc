@import gg.jte.support.LocalizationSupport
@import io.github.gadnex.datastarspringmvc.person.Person
@import org.springframework.validation.BindingResult
@import org.springframework.validation.FieldError

@param LocalizationSupport localizer
@param Person person
@param BindingResult result

@template.layout.Layout(localizer = localizer, currentPath = "/add-person", content = @`
    <div class="stack">
        <h2 class="text-3">Add person</h2>
        <form action="/add-person" method="post" class="box stack">
            @if(result == null)
                <div>
                    <div>
                        <label for="name">${localizer.localize("Person.field.name")}</label>
                    </div>
                    <div>
                        <input id="name" name="name" class="w-full" autofocus>
                    </div>
                </div>
                <div>
                    <div>
                        <label for="email">${localizer.localize("Person.field.email")}</label>
                    </div>
                    <div>
                        <input id="email" type="email" name="email" class="w-full">
                    </div>
                </div>
            @else
                <div>
                    <div>
                        <label for="name">${localizer.localize("Person.field.name")}</label>
                    </div>
                    <div>
                        @if(result.getFieldError("name") != null)
                            <input id="name"
                                   name="name"
                                   value="${person.name()}"
                                   aria-invalid="true"
                                   class="w-full">
                            <ul class="m-i-0 m-b-2">
                                @for(FieldError fieldError : result.getFieldErrors("name"))
                                    <li>${fieldError.getDefaultMessage()}</li>
                                @endfor
                            </ul>
                        @else
                            <input id="name"
                                   name="name"
                                   value="${person.name()}"
                                   aria-invalid="false"
                                   class="w-full">
                        @endif
                    </div>
                </div>
                <div>
                    <div>
                        <label for="email">${localizer.localize("Person.field.email")}</label>
                    </div>
                    <div>
                        @if(result.getFieldError("email") != null)
                            <input id="email"
                                   name="email"
                                   value="${person.email()}"
                                   aria-invalid="true"
                                   class="w-full">
                            <ul class="m-i-0 m-b-2">
                                @for(FieldError fieldError : result.getFieldErrors("email"))
                                    <li>${fieldError.getDefaultMessage()}</li>
                                @endfor
                            </ul>
                        @else
                            <input id="email"
                                   name="email"
                                   value="${person.email()}"
                                   aria-invalid="false"
                                   class="w-full">
                        @endif
                    </div>
                </div>
            @endif
            <div class="cluster main-end">
                <button type="submit" class="primary">Submit</button>
            </div>
        </form>
    </div>
`)