function authenticate() {
    return gapi.auth2.getAuthInstance()
        .signIn({scope: "https://www.googleapis.com/auth/calendar https://www.googleapis.com/auth/calendar.events"})
        .then(function() { console.log("Sign-in successful"); },
            function(err) { console.error("Error signing in", err); });
}
function loadClient() {
    gapi.client.setApiKey("");
    return gapi.client.load("https://content.googleapis.com/discovery/v1/apis/calendar/v3/rest")
        .then(function() { console.log("GAPI client loaded for API"); },
            function(err) { console.error("Error loading GAPI client for API", err); });
}
// Make sure the client is loaded and sign-in is complete before calling this method.
function execute() {
    var email = document.getElementById("email").value;
    var startDate = new Date(document.getElementById("startDate").value).toISOString().replace(".000", "");
    var endDate = new Date(document.getElementById("endDate").value).toISOString().replace(".000", "");
    var title = document.getElementById("title").value;
    var description = document.getElementById("description").value;
    return gapi.client.calendar.events.insert({
        "calendarId": email,
        "resource": {
            "summary" : title,
            "description": description,
            "end": {
                "dateTime": endDate
            },
            "start": {
                "dateTime": startDate
            },
            "reminders": {
                "useDefault": true
            }
        }
    })
        .then(function(response) {
                // Handle the results here (response.result has the parsed body).
                console.log("Response", response);
            },
            function(err) { console.error("Execute error", err); });
}
gapi.load("client:auth2", function() {
    gapi.auth2.init({client_id: ""});
});