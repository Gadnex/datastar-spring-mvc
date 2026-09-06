@import gg.jte.support.LocalizationSupport

@param String nonce
@param LocalizationSupport localizer
@param Integer status
@param String error
@param String message

@template.layout.Layout(nonce = nonce, localizer = localizer, currentPath = "/error", content = @`
    <h2>HTTP ${status} - ${error}</h2>
    <p>${message}</p>
`)
