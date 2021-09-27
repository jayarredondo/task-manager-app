var todaysDate = new Date().toISOString();
console.log(todaysDate)
let minDate = todaysDate.replace(todaysDate.substring(16, 24), "");
console.log(minDate)
if (document.getElementById("startDate")) {
    let startInput = document.getElementById("startDate");
    document.getElementById("startDate").setAttribute("min", minDate);
    startInput.addEventListener("change", function (){
        let endDateMin = document.getElementById("startDate").value;
        console.log(endDateMin)
        document.getElementById("endDate").setAttribute("min", endDateMin);
    })
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



