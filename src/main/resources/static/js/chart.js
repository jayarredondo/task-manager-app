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


    let tasks = [
        {
            name: 'Finance',
            size: financialTasks
        },
        {
            name: 'Health',
            size: healthTasks
        },
        {
            name: 'Work',
            size: workTasks
        },
        {
            name: 'Social',
            size: socialTasks
        },
        {
            name: 'Family',
            size: familyTasks
        },
        {
            name: 'Misc.',
            size: miscTasks
        },
        {
            name: 'Academic',
            size: academicTasks
        },
        {
            name: 'Hobbies',
            size: hobbyTasks
        }
        ];

    const findApplicable =  (tasks) => {
        let applicableNameTasks = [];
        let applicableSizeTasks = [];
        tasks.forEach(function(task) {
            if(task.size != 0) {
                applicableNameTasks.push(task.name);
                applicableSizeTasks.push(parseInt(task.size));
            }
        })
        return [applicableNameTasks, applicableSizeTasks];
    }

    var applicableTasks = findApplicable(tasks);
    console.log(applicableTasks)

    let options2 = {
        chart: {
            height: 250,
            type: 'donut'
        },
        series: applicableTasks[1],
        labels: applicableTasks[0],
    }

    var chart2 = new ApexCharts(document.querySelector("#donutChart"), options2);
    chart2.render();
});