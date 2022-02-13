document.addEventListener('DOMContentLoaded', function () {
    loadDoneTasks().then(function () {
        loadPercentageOfIssues(4);
    });
});

backgroundColors = [
    'rgba(255, 99, 132, 0.2)',
    'rgba(54, 162, 235, 0.2)',
    'rgba(255, 206, 86, 0.2)',
    'rgba(75, 192, 192, 0.2)',
    'rgba(153, 102, 255, 0.2)',
    'rgba(255, 159, 64, 0.2)'
]

borderColors = [
    'rgba(255, 99, 132, 1)',
    'rgba(54, 162, 235, 1)',
    'rgba(255, 206, 86, 1)',
    'rgba(75, 192, 192, 1)',
    'rgba(153, 102, 255, 1)',
    'rgba(255, 159, 64, 1)'
]

async function loadDoneTasks() {
    const request = await fetch('../api/manageSprints/getDoneTasks', {
        method: 'GET',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'Authorization': localStorage.token
        }
    });

    request.json().then(function (response) {
        if (response.status) {

            // backgrounds and borders that are going to be send to the chart function
            backgrounds = [];
            borders = [];

            for (let i = 0; i < response.dataset.length; i++) {
                backgrounds.push(backgroundColors[i]);
                borders.push(borderColors[i]);
            }

            // labels and data that are going to be send to the chart function
            labels = [];
            data = [];

            response.dataset.map(function (row) {
                labels.push(row[0]);
                data.push(row[1]);
            });

            // sending the data to the function
            barChart('chart1', labels, data, backgrounds, borders);

        } else {
            Swal.fire('Error', response.exception, 'error');
        }
    });
}

async function loadPercentageOfIssues(id) {
    const request = await fetch(`../api/manageSprints/getTasksByProject?id=${id}`, {
        method: 'GET',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'Authorization': localStorage.token
        }
    });

    request.json().then(function (response) {
        if (response.status) {

            // backgrounds and borders that are going to be send to the chart function
            backgrounds = [];
            borders = [];

            for (let i = 0; i < response.dataset.length; i++) {
                backgrounds.push(backgroundColors[i]);
                borders.push(borderColors[i]);
            }

            // labels and data that are going to be send to the chart function
            labels = [];
            data = [];

            response.dataset.map(function (row) {
                labels.push(row[0]);
                data.push(row[1].toFixed(2));
            });

            

            document.getElementById('projectChartDiv').removeChild(document.getElementById('chart2'));

            var graph = document.createElement('canvas');
            graph.id = 'chart2';
            graph.width = '400';
            graph.height = '400';

            document.getElementById('projectChartDiv').appendChild(graph);

            closeModal('changeChartModal');

            // sending the data to the function
            doughnutChart('chart2', labels, data, backgrounds, borders);

            document.getElementById('lblProjectName').textContent = response.dataset[0][2];

        } else {
            Swal.fire('Error', response.exception, 'error');
        }
    });
}

function barChart(canvas, labels, data, backgroundColors, borderColors) {
    const ctx = document.getElementById(canvas);
    const myChart = new Chart(ctx, {
        type: 'bar',
        data: {
            labels: labels,
            datasets: [{
                label: 'Done tasks',
                data: data,
                backgroundColor: backgroundColors,
                borderColor: borderColors,
                borderWidth: 1
            }]
        },
        options: {
            scales: {
                y: {
                    grid: {
                        display: false
                    }
                },
                x: {
                    grid: {
                        display: false
                    }
                }
            }
        }
    });
}

function doughnutChart(canvas, labels, data, backgroundColors, borderColors) {
    const ctx = document.getElementById(canvas);
    const myChart = new Chart(ctx, {
        type: 'doughnut',
        data: {
            labels: labels,
            datasets: [{
                label: '# of Votes',
                data: data,
                backgroundColor: backgroundColors,
                borderColor: borderColors,
                borderWidth: 1
            }]
        },
        options: {
            scales: {
                y: {
                    grid: {
                        display: false
                    }
                },
                x: {
                    grid: {
                        display: false
                    }
                }
            },
            plugins: {
                tooltip: {
                    callbacks: {
                        label: function(tooltipItem){
                            var label = myChart.data.labels[tooltipItem.dataIndex];
                            var value = myChart.data.datasets[tooltipItem.datasetIndex].data[tooltipItem.dataIndex];
                            return label+ ': ' + value + '%'; 
                        }
                    }
                }
            }
        }
    });
}

async function loadTable(){
    const request = await fetch('../api/manageSprints/getProjectsWithTasks',{
        method: 'GET',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'Authorization': localStorage.token
        }
    });

    request.json().then(function(response){
        if (response.status) {
            let content = '';
            response.dataset.map(function(row){
                content += `
                <tr>
                    <th scope="row" class="id-padding">${row[0]}</th>
                    <td>${row[1]}</td>
                    <td>${row[2]}</td>
                    <th scope="row">
                        <div>
                            <a onclick="loadPercentageOfIssues(${row[0]})" class="btn btn-sm custom-btn" data-bs-toggle="tooltip" data-bs-placement="top" title="Update"><i class="bi bi-eye-fill"></i></a>
                        <div>
                    </th>
                </tr>
                `
            });

            document.getElementById('tbody-project').innerHTML = content;
            
            openModal('changeChartModal')
            
        } else {
            Swal.fire('Error',response.exception, 'error');
        }
    });
}