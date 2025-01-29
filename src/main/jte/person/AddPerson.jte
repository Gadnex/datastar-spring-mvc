@import gg.jte.support.LocalizationSupport
@import io.github.gadnex.datastarspringmvc.person.Person
@import org.springframework.validation.BindingResult

@param LocalizationSupport localizer
@param Person person
@param BindingResult result

@template.layout.Layout(localizer = localizer, content = @`
    <h2>Add person</h2>
    <form action="/add-person" method="post">
        @if(result == null)
            <label>
                Name
                <input name="name">
            </label>
            <label>
                Email
                <input type="email" name="email">
            </label>
        @else
            <label>
                Name
                @if(result.getFieldError("name") != null)
                    <input name="name" value="${person.name()}" aria-invalid="true">
                    <small>${result.getFieldError("name").getDefaultMessage()}</small>
                @else
                    <input name="name" value="${person.name()}" aria-invalid="false">
                @endif
            </label>
            <label>
                Email
                @if(result.getFieldError("email") != null)
                    <input name="email" value="${person.email()}" aria-invalid="true">
                    <small>${result.getFieldError("email").getDefaultMessage()}</small>
                @else
                    <input name="email" value="${person.email()}" aria-invalid="false">
                @endif
            </label>
        @endif
        <button>Submit</button>
    </form>
`)