@import gg.jte.support.LocalizationSupport
@import io.github.gadnex.datastarspringmvc.person.Person
@import org.springframework.validation.BindingResult

@param String datastar
@param LocalizationSupport localizer
@param Person person
@param BindingResult result

<div id="person">
    <h2>Add person</h2>
    @if(datastar == null)
        <form action="/add-person" method="post">
            @template.person.AddPersonFields(localizer = localizer, person = person, result = result)
        </form>
    @elseif(datastar.equals("redirect"))
        <form action="/add-person"
              method="post"
              data-on-submit="@post('/data-star/add-person?datastar=redirect', {contentType: 'form'})">
            @template.person.AddPersonFieldsDatastar(datastar = datastar, localizer = localizer, person = person, result = result)
        </form>
    @else
        <form action="/add-person"
              method="post"
              data-on-submit="@post('/data-star/add-person?datastar=push-url', {contentType: 'form'})">
            @template.person.AddPersonFieldsDatastar(datastar = datastar, localizer = localizer, person = person, result = result)
        </form>
    @endif
</div>