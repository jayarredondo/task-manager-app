(function () {

    // DATE FORMAT
    const dateStrings = document.getElementsByClassName("date");
    const formatDate = (dateString) => {
        let s =  new Date(dateString).toLocaleString();
        let datetime = s.split(",");
        let date = new Date(datetime[0]).toDateString();
        let time = datetime[1];
        return date + ", " + time;
    }

    if(dateStrings.length != 0) {
        for (let i = 0; i <= dateStrings.length; i++) {
            dateStrings[i].innerHTML = formatDate(dateStrings[i].innerHTML);
        }
    }
    // -----------

    var currentdate = new Date();
    var datetime =currentdate.getFullYear() + "-"
        + (currentdate.getMonth()+1)  + "-"
        + currentdate.getDate() + "T"
        + currentdate.getHours() + ":"
        + currentdate.getMinutes() + ":"
        + currentdate.getSeconds();
    document.getElementById("currentDate").value = datetime;

})();