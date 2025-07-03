@param int connectionCount = 1
@if (connectionCount > 1)
    <div id="connections">
        ${connectionCount}
    </div>
@else
    <div id="connections" class="hidden">
        ${connectionCount}
    </div>
@endif