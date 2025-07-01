@import gg.jte.Content
@import gg.jte.support.LocalizationSupport

@param LocalizationSupport localizer
@param Content content 

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>${localizer.localize("application")}</title>
    <link rel="stylesheet"
          href="/webjars/picocss__pico/css/pico.min.css">
    <link rel="stylesheet"
          href="/webjars/picocss__pico/css/pico.colors.min.css">
    <link rel="stylesheet"
          href="/webjars/material-icons-font/material-icons-font.css">
    <link rel="stylesheet" href="/style.css">
    <script type="module" src="/js/datastar.js"></script>
</head>
<body>
<header class="container-fluid pico-background-azure-750">
    <nav>
        <h1 class="pico-background-azure-750">${localizer.localize("application")}</h1>
        @template.layout.Menu(localizer = localizer)
    </nav>
</header>
<main class="container">
    ${content}
</main>
</body>
</html>