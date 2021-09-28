// Restricting Date inputs to current datetime and onward, and end date to be from start datetime onward.
var todaysDate = new Date().toISOString();
let minDate = todaysDate.replace(todaysDate.substring(16, 24), "");
if (document.getElementById("startDate")) {
    let startInput = document.getElementById("startDate");
    document.getElementById("startDate").setAttribute("min", minDate);
    startInput.addEventListener("change", function (){
        let endDateMin = document.getElementById("startDate").value;
        document.getElementById("endDate").setAttribute("min", endDateMin);
    })
}
// Changing box shadows to display urgency of task due date
let dueDate = document.getElementsByClassName("startDateTime");
console.log(dueDate)
const defineTaskUrgency = (startDateArray) => {
    let currentDate = new Date().getTime();
    Array.from(startDateArray).forEach(date => {
        let startDate = new Date(date.innerText);
        let dueDate = new Date(date.innerText).getTime();
        let differenceInTime = dueDate - currentDate;
        let differenceInDays = Math.trunc(differenceInTime / (1000 * 3600 * 24));
        let cardToStyle = date.parentNode.parentNode.parentNode.parentNode;
        console.log("Today: " + Date.now(), "Start Date: " + startDate, "difference: " + differenceInDays)
        if (differenceInDays === 0) {
            cardToStyle.classList.add('red-indicator');
        } else if (differenceInDays > 0 && differenceInDays < 3) {
            cardToStyle.classList.add('orange-indicator');
        } else if(differenceInDays >= 3 && differenceInDays < 7) {
            cardToStyle.classList.add('yellow-indicator');
        } else if(differenceInDays >= 7) {
            cardToStyle.classList.add('green-indicator');
        } else {
            cardToStyle.classList.add('past-due')
        }
    })
}
defineTaskUrgency(dueDate);
// 7+ days = green
// 3-6 days = yellow
// 1-2 days = orange
// day of = red


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


