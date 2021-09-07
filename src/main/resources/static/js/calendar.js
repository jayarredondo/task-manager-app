"use strict";
var events = [];
$('.task').each(function (i){
    let id, title, start, end;


    id = $(this).children().first().text();
    let url = "/tasks/" + id;
    title = $(this).children('.taskTitle').first().text();
    start = $(this).children('.taskStart').first().text().toLocaleString('en-US');
    end = $(this).children('.taskEnd').first().text().toLocaleString('en-US');
    events.push({title, start, end, url});
});

document.addEventListener('DOMContentLoaded', function() {
    var calendarEl = document.getElementById('calendar');

    var calendar = new FullCalendar.Calendar(calendarEl, {
        initialView: 'dayGridMonth',
        headerToolbar: {
            left: 'prev,next today',
            center: 'title',
            right: 'dayGridMonth,timeGridWeek,timeGridDay'
        },
        eventSources: [
            {
                events
            }
        ],
        eventClick: function (info) {
            // alert('Event Address: ' + info.event.title + "\n" + ' Event Start: ' + info.event.start.toLocaleString('en-US') + "\n" + ' Event End: ' + info.event.end.toLocaleString('en-US'));
            var id = info.event.id
            // window.location.href = "/events/{info.event.id}";
            window.location.href = info.event.url;

            // change the border color just for fun
            info.el.style.borderColor = 'red';
        }
    });

    calendar.render();
});