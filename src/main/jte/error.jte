@import gg.jte.support.LocalizationSupport

@param LocalizationSupport localizer
@param Integer status
@param String error
@param String message

@template.layout.Layout(localizer = localizer, content = @`
    <h2>HTTP ${status} - ${error}</h2>
    <p>${message}</p>
`)
