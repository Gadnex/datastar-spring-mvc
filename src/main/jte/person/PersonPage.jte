@import gg.jte.support.LocalizationSupport
@import io.github.gadnex.datastarspringmvc.person.Person

@param LocalizationSupport localizer
@param Person person

@template.layout.Layout(localizer = localizer, content = @`
    @template.person.Person(localizer = localizer, person = person, fragment = false)
`)