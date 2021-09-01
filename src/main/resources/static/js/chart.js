"use strict";

window.addEventListener("load", function () {

    // Today's Progress Radial Bar Chart

    let totalIncompleteTasks = parseInt(document.getElementById("todaysIncompleteTasks").value);
    let totalCompleteTasks = parseInt(document.getElementById("todaysCompleteTasks").value);
    let todaysTotalTasks = totalIncompleteTasks + totalCompleteTasks;
    let progress = ((totalCompleteTasks / todaysTotalTasks) * 100)

    let options1 = {
        chart: {
            height: 350,
            type: 'radialBar',
        },
        series: [progress],
        labels: ['Completion'],
    }

    let chart1 = new ApexCharts(document.querySelector("#radialBarChart"), options1);

    chart1.render();

    // Task Categories Donut Chart

    let workTasks = document.getElementById("workTasks").value;
    let healthTasks = document.getElementById("healthTasks").value;
    let financialTasks = document.getElementById("financialTasks").value;
    let socialTasks = document.getElementById("socialTasks").value;
    let familyTasks = document.getElementById("familyTasks").value;
    let academicTasks = document.getElementById("academicTasks").value;
    let miscTasks = document.getElementById("miscTasks").value;
    let hobbyTasks = document.getElementById("hobbyTasks").value;

    console.log(workTasks);

    var ctx = document.getElementById('donutChart');
    var myChart = new Chart(ctx, {
        type: 'doughnut',
        data: {
            labels: [
                'Finance',
                'Health',
                'Family',
                'Social',
                'Work',
                'Academic',
                'Hobbies',
                'Misc.'
            ],
            datasets: [{
                label: 'My First Dataset',
                data: [financialTasks, healthTasks, familyTasks, socialTasks, workTasks, academicTasks, hobbyTasks, miscTasks],
                backgroundColor: [
                    'Orange',
                    'Green',
                    'Purple',
                    'Yellow',
                    'Red',
                    'hotpink',
                    'Blue',
                    'Brown'

                ],
                hoverOffset: 4
            }]
        },
    });
});