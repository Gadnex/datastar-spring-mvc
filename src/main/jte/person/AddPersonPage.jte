@import gg.jte.support.LocalizationSupport
@import io.github.gadnex.datastarspringmvc.person.Person
@import org.springframework.validation.BindingResult

@param String datastar
@param LocalizationSupport localizer
@param Person person
@param BindingResult result

@template.layout.Layout(localizer = localizer, content = @`
    @template.person.AddPerson(datastar = datastar, localizer = localizer, person = person, result = result)
`)