var todaysDate = new Date();
var year = todaysDate.getFullYear()
var month = ("0" + (todaysDate.getMonth() + 1)).slice(-2);
var day = ("0" + todaysDate.getDate()).slice(-2);

let minDate = (year +"-"+ month +"-"+ day);
console.log(minDate);

if (document.getElementById("startDate")) {
    let startInput = document.getElementById("startDate");
    document.getElementById("startDate").setAttribute("min", minDate);
    startInput.addEventListener("change", function (){
        let endDateMin = new Date(document.getElementById("startDate").value);
        console.log(endDateMin.toISOString())
        let year = endDateMin.getFullYear()
        let month = ("0" + (endDateMin.getMonth() + 1)).slice(-2);
        let day = ("0" + endDateMin.getDate()).slice(-2);

        let afterStart = (year +"-"+ month +"-"+ day);

        document.getElementById("endDate").setAttribute("min", afterStart);
    })
}

function restrictStartDate(e) {
    let endDateMin = new Date(document.getElementById("startDate").value);
}


// DATE FORMAT
const dateStrings = document.getElementsByClassName("date");
const formatDate = (dateString) => {
    let s = new Date(dateString).toLocaleString();
    let datetime = s.split(",");
    let date = new Date(datetime[0]).toDateString();
    let time = datetime[1];
    return date + ", " + time;
}

if (dateStrings.length != 0) {
    for (let i = 0; i <= dateStrings.length; i++) {
        dateStrings[i].innerHTML = formatDate(dateStrings[i].innerHTML);
    }
}
// -----------



