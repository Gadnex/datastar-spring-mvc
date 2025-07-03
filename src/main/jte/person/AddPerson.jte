@import gg.jte.support.LocalizationSupport
@import io.github.gadnex.datastarspringmvc.person.Person
@import org.springframework.validation.BindingResult

@param LocalizationSupport localizer
@param Person person
@param BindingResult result

@template.layout.Layout(localizer = localizer, content = @`
    <div id="person">
        <h2>Add person</h2>
        <form action="/add-person" method="post">
            @if(result == null)
                <label>
                    ${localizer.localize("Person.field.name")}
                    <input name="name">
                </label>
                <label>
                    ${localizer.localize("Person.field.email")}
                    <input type="email" name="email">
                </label>
            @else
                <label>
                    ${localizer.localize("Person.field.name")}
                    @if(result.getFieldError("name") != null)
                        <input name="name"
                               value="${person.name()}"
                               aria-invalid="true">
                        <small>${result.getFieldError("name").getDefaultMessage()}</small>
                    @else
                        <input name="name"
                               value="${person.name()}"
                               aria-invalid="false">
                    @endif
                </label>
                <label>
                    ${localizer.localize("Person.field.email")}
                    @if(result.getFieldError("email") != null)
                        <input name="email"
                               value="${person.email()}"
                               aria-invalid="true">
                        <small>${result.getFieldError("email").getDefaultMessage()}</small>
                    @else
                        <input name="email"
                               value="${person.email()}"
                               aria-invalid="false">
                    @endif
                </label>
            @endif
            <button>Submit</button>
        </form>
    </div>
`)