/* Open when someone clicks on the span element */
function openNav() {
    document.getElementById("myNav").style.width = "100vw";
}

/* Close when someone clicks on the "x" symbol inside the overlay */

function closeNav() {
    document.getElementById("myNav").style.width = "0";
}

let dailyItems = document.getElementsByClassName("dailyItems");
if(dailyItems.length === 0) {
    if(document.getElementById("dailyItemList")) {
        document.getElementById("dailyItemList").innerHTML = '<h6>Looks like you dont have any daily items set up just yet, click on the manage button to add!</h6>';
        document.getElementById("dailyResetButton").style.display = "none";
    }
} else {
    Array.from(dailyItems).forEach(item => {
        if (item.value === "true") {
            console.log(item.value);
            console.log("This item is complete and should be checked.");
            item.setAttribute("checked", "checked");
            item.setAttribute("disabled", "disabled");
            item.nextElementSibling.style.textDecoration = "line-through";
            item.nextElementSibling.classList.add("text-muted");
        }
    });
}

// Restricting Date inputs to current datetime and onward, and end date to be from start datetime onward.
var todaysDate = new Date().toISOString();
let minDate = todaysDate.replace(todaysDate.substring(11, 24), "00:00");
console.log(minDate);
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
const defineTaskUrgency = (startDateArray) => {
    let currentDate = new Date().getTime();
    Array.from(startDateArray).forEach(date => {
        let startDate = new Date(date.innerText);
        let dueDate = new Date(date.innerText).getTime();
        let differenceInTime = dueDate - currentDate;
        let differenceInDays = Math.trunc(differenceInTime / (1000 * 3600 * 24));
        let cardToStyle = date.parentNode.parentNode.previousSibling.previousSibling.lastChild
        if (differenceInDays <= 0) {
            cardToStyle.classList.add('red-indicator');
        } else if (differenceInDays > 0 && differenceInDays < 3) {
            cardToStyle.classList.add('orange-indicator');
        } else if(differenceInDays >= 3 && differenceInDays < 7) {
            cardToStyle.classList.add('yellow-indicator');
        } else if(differenceInDays >= 7) {
            cardToStyle.classList.add('green-indicator');
        }
    })
}
// Call function to add styling
defineTaskUrgency(dueDate);



// DATE FORMAT
const dateStrings = document.getElementsByClassName("date");
const formatDate = (dateString) => {
    let s = new Date(dateString).toLocaleString();
    let datetime = s.split(",");
    let date = new Date(datetime[0]).toDateString();
    let time = datetime[1];
    return date + ", " + time;
}

if (dateStrings.length !== 0) {
    for (let i = 0; i <= dateStrings.length; i++) {
        if(dateStrings[i]){
            dateStrings[i].innerHTML = formatDate(dateStrings[i].innerHTML);
        }
    }
}


