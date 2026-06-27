@import gg.jte.Content
@import gg.jte.support.LocalizationSupport

@param LocalizationSupport localizer
@param String currentPath
@param Content content 

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>${localizer.localize("application")}</title>
    <link rel="stylesheet" href="/css/constellation.min.css">
    <link rel="stylesheet" href="/css/style.css">
    <script type="module" src="/js/datastar.js"></script>
</head>
<body class="fullscreen center p-0">
<header class="cluster main-space-between cross-center container p-4 bg-primary-2">
    <h1 class="mobile-grow text-3 text-center">${localizer.localize("application")}</h1>
    @template.layout.Menu(localizer = localizer, currentPath = currentPath)
</header>
<main class="p-4">
    ${content}
</main>
<div data-on:keydown__window="evt.ctrlKey && evt.key === 'ArrowUp' && @get('/chaos/on')"></div>
<div data-on:keydown__window="evt.ctrlKey && evt.key === 'ArrowDown' && @get('/chaos/off')"></div>
</body>
</html>