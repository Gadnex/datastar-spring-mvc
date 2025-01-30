@import gg.jte.support.LocalizationSupport
@import io.github.gadnex.datastarspringmvc.person.Person
@import org.springframework.validation.BindingResult

@param String datastar
@param LocalizationSupport localizer
@param Person person
@param BindingResult result

@if(result == null)
    <label>
        ${localizer.localize("Person.field.name")}
        <input name="name" data-bind="name">
    </label>
    <label>
        ${localizer.localize("Person.field.email")}
        <input type="email" name="email" data-bind="email">
    </label>
@else
    <label>
        ${localizer.localize("Person.field.name")}
        @if(result.getFieldError("name") != null)
            <input name="name"
                   value="${person.name()}"
                   aria-invalid="true"
                   data-bind="name">
            <small>${result.getFieldError("name").getDefaultMessage()}</small>
        @else
            <input name="name"
                   value="${person.name()}"
                   aria-invalid="false"
                   data-bind="name">
        @endif
    </label>
    <label>
        ${localizer.localize("Person.field.email")}
        @if(result.getFieldError("email") != null)
            <input name="email"
                   value="${person.email()}"
                   aria-invalid="true"
                   data-bind="email">
            <small>${result.getFieldError("email").getDefaultMessage()}</small>
        @else
            <input name="email"
                   value="${person.email()}"
                   aria-invalid="false"
                   data-bind="email">
        @endif
    </label>
@endif
<button>Submit</button>