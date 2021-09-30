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
    let choreTasks = document.getElementById("choreTasks").value;
    let academicTasks = document.getElementById("academicTasks").value;
    let miscTasks = document.getElementById("miscTasks").value;
    let hobbyTasks = document.getElementById("hobbyTasks").value;

    var ctx = document.getElementById('donutChart');
    var myChart = new Chart(ctx, {
        type: 'doughnut',
        data: {
            labels: [
                'Finance',
                'Health',
                'Chores',
                'Social',
                'Work',
                'Academic',
                'Hobbies',
                'Misc.'
            ],
            datasets: [{
                label: 'Tasks',
                data: [financialTasks, healthTasks, choreTasks, socialTasks, workTasks, academicTasks, hobbyTasks, miscTasks],
                backgroundColor: [
                    '#F6511D',
                    '#FFB400',
                    '#00A6ED',
                    '#7FB800',
                    '#0D2C54',
                    '#9368b7',
                    '#F2BAC9',
                    '#006400'

                ],
                hoverOffset: 4
            }]
        },
        options: {
            plugins: {
                legend: {
                    position: 'left',
                    labels: {
                        usePointStyle: true,
                        font: {
                            family: "Helvetica Neue"
                        }
                    }
                }
            }
        }
    });
});