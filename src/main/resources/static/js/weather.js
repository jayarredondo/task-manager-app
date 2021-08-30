"use strict";

$.get("https://api.openweathermap.org/data/2.5/weather", {
    APPID: file,
    units: 'imperial',
    q: 'San Antonio',
}).done(function(resp) {
        $('#weather-info').append(
            `<div class="col text-center">` +
            '<h5>' + new Date(resp.dt * 1000).toDateString() + `</h5>` +
            '<img style="width:10vh; height: auto" src="http://openweathermap.org/img/w/' + resp.weather[0].icon + '.png">'+
            `<h1>`+ Math.round(resp.main.temp) + '°</h1>' +
            '<p class="text-muted">Feels like: ' + Math.round(resp.main.feels_like) + '</p>' +
            '</div>'
        )
    });