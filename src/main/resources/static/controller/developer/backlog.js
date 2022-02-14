document.addEventListener('DOMContentLoaded',function(){
    makeRequests();
});

function makeRequests(){
    loadProjectData().then(function(){
        loadSprints().then(function(){
            fillSelect('../api/manageSprints/loadPriorities', 'priority', null).then(function(){
                fillSelect('../api/manageSprints/loadTypes', 'type', null)
            });
        });
    });
}

async function loadProjectData(){
    const request = await fetch(`../api/projects/getProjectDetails?id=${document.getElementById('id_project').value}`,{
        method: 'GET',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'Authorization': localStorage.token
        }
    });

    request.json().then(function(response){
        if (response.status) {
            document.getElementById('project_name').textContent = response.dataset[0][2];
            document.getElementById('description').textContent = response.dataset[0][3];
        } else {
            Swal.fire('Error!',response.exception, 'error');
        }
    });
}

async function loadSprints(){
    const request = await fetch(`../api/manageSprints/getTasksOfASprint?id=${document.getElementById('id_project').value}`,{
        method: 'GET',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'Authorization': localStorage.token
        }
    });

    request.json().then(function(response){
        if (response.status) {
            console.log(response.dataset);
            let content = '';
            response.dataset.map(function(row){
                content += `
                <div class="col-12 mb-3 p-4 animate__animated animate__fadeInUp w-75" style="border: 2px solid #E8E8E8;">
                    <div class="row">
                        <div class="col-6 d-flex justify-content-start align-items-center">
                            <h5>${row.sprint_name}</h5>
                        </div>
                        <div class="col-6 d-flex justify-content-end">
                            <button onclick="openTasksModal(${row.id_sprint})" class="btn custom-btn pe-3 mt-2">
                                <div class="d-flex justify-content-center align-items-center">
                                    <i class="bi bi-journal-plus icon-color me-3"></i><span>Add task</span>
                                </div>
                            </button>
                        </div>
                    </div>
                    <table class="table table-borderless mt-3">
                        <thead>
                            <tr>
                                <th scope="col"></th>
                                <th scope="col">Type</th>
                                <th scope="col">Priority</th>
                                <th scope="col">Summary</th>
                                <th scope="col">Status</th>
                                <th scope="col">Actions</th>
                            </tr>
                        </thead>
                        <tbody id="tbody-managers">
                           `
                row.tasks.map(function(row){
                    content += `<tr>`
                    // for icon selection
                    if (row[3] == 'Bug') {
                        content+=`<th scope="row" class="id-padding"><i class="bi bi-bug h5"></i></th>
                                  <td>${row[3]}</td>`;
                    } else if (row[3] == 'New Feature'){
                        content+=`<th scope="row" class="id-padding"><i class="bi bi-bookmark-plus h5"></i></th>
                                  <td>${row[3]}</td>`;
                    } else if (row[3] == 'Improvement'){
                        content+=`<th scope="row" class="id-padding"><i class="bi bi-boxes h5"></i></th>
                                  <td>${row[3]}</td>`;
                    } else if (row[3] == 'Task') {
                        content+=`<th scope="row" class="id-padding"><i class="bi bi-journal-check h5"></i></th>
                                  <td>${row[3]}</td>`;
                    }

                    // for priority color
                    if (row[2] == 'High') {
                        content += `<td style="color: red;">${row[2]}</td>`
                    } else if(row[2] == 'Medium') {
                        content += `<td style="color: orange;">${row[2]}</td>`
                    } else if (row[2] == 'Low') {
                        content += `<td style="color: green;">${row[2]}</td>`
                    }
                    content += `   
                                    <td>${row[1]}</td>
                                    <td>${row[4]}</td>
                                    <th scope="row"><a onclick="getTaskInfo(${row[0]})" class="btn btn-sm custom-btn" data-bs-toggle="tooltip" data-bs-placement="top" title="See details"><i class="bi bi-eye-fill"></i></a></th>
                                </tr>`
                });
                content+= ` 
                        </tbody>
                    </table>
                </div>
                `
            });

            document.getElementById('tbody-backlog').innerHTML = content;
        } else {
            Swal.fire('Error!',response.exception, 'error').then(function(){
                redirect('dashboard');
            });
        }
    });
}

function openTasksModal(id){
    document.getElementById('id_development_cycle').value = id;
    openModal('createTaskModal');
}

function getTaskInfo(id){

    fetch(`../api/manageSprints/getTaskInfo?id=${id}`,{
        method: 'GET',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'Authorization': localStorage.token
        }
    }).then(function(request){
        request.json().then(function(response){
            if (response.status) {
                console.log(response.dataset);
                document.getElementById('id_task').value = response.dataset[0][0];
                document.getElementById('lblSummary').textContent = response.dataset[0][1];
                document.getElementById('lblPriority').textContent = response.dataset[0][2];
                document.getElementById('lblType').textContent = response.dataset[0][3];
                document.getElementById('lblStatus').textContent = response.dataset[0][4];
                document.getElementById('lblCreatedBy').textContent = response.dataset[0][5];
                document.getElementById('lblDescription').textContent = response.dataset[0][7];

                if (response.dataset[0][4] == 'To Do') {

                    document.getElementById('toDoOptions').classList.remove('d-none');
                    document.getElementById('doingOptions').classList.add('d-none');
                    document.getElementById('doneOptions').classList.add('d-none');

                } else if (response.dataset[0][4] == 'Doing') {

                    document.getElementById('toDoOptions').classList.add('d-none');
                    document.getElementById('doingOptions').classList.remove('d-none');
                    document.getElementById('doneOptions').classList.add('d-none');

                    document.getElementById('lblIssueOwner1').textContent = response.dataset[0][6];

                } else if (response.dataset[0][4] == 'Done') {

                    document.getElementById('toDoOptions').classList.add('d-none');
                    document.getElementById('doingOptions').classList.add('d-none');
                    document.getElementById('doneOptions').classList.remove('d-none');

                    document.getElementById('lblIssueOwner2').textContent = response.dataset[0][6];
                }

                openModal('manageTasksModal');
            } else {
                Swal.fire('Warning!',response.exception, 'warning');
            }
        });
    });
}

document.getElementById('taskInfo-form').addEventListener('submit',function(event){
    event.preventDefault();

    if (!document.getElementById('toDoOptions').classList.contains('d-none')) {
        assignTask();
    } else if (!document.getElementById('doingOptions').classList.contains('d-none')) {
        endTask();
    }
    
});

function simulateClick(){
    document.getElementById('btnSubmitCreateTask-form').click();
}

document.getElementById('createTask-form').addEventListener('submit',function(event){
    event.preventDefault();

    data = {}
    data.summary = document.getElementById('summary').value;
    data.description = document.getElementById('description2').value;
    data.id_priority = document.getElementById('priority').value;
    data.id_type = document.getElementById('type').value;
    data.id_development_cycle = document.getElementById('id_development_cycle').value;
    data.id_project = document.getElementById('id_project').value;

    fetch('../api/manageSprints/saveTask', {
        method: 'POST',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'Authorization': localStorage.token
        },
        body: JSON.stringify(data)
    }).then(function(request){
        // parses request to json
        request.json().then(function (response) {
        // checks response status
        if (response.status) {
            closeModal('createTaskModal');
            loadSprints();
            Swal.fire('Success!', response.message, 'success')
        } else {
            Swal.fire('Warning!', response.exception, 'warning');
        }
    });
    });
});

function assignTask(){
    Swal.fire({
        title: 'Assign Task',
        text: "Are you sure that you want to do this task?",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Yes, let me do it.'
    }).then((result) => {
        if (result.isConfirmed) {

            fetch(`../api/manageSprints/assignTask?id=${document.getElementById('id_task').value}`, {
                method: 'GET',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json',
                    'Authorization': localStorage.token
                }
            }).then(function (request) {
                request.json().then(function (response) {
                    // checks response status
                    if (response.status) {
                        closeModal('manageTasksModal');
                        loadSprints();
                        Swal.fire('Success!', response.message, 'success');
                    } else {
                        Swal.fire('Warning!', response.exception, 'warning');
                    }
                });
            });
        }
    });
}

function endTask(){
    Swal.fire({
        title: 'Assign Task',
        text: "Are you sure that you want to end this task?",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Yes, let me do it.'
    }).then((result) => {
        if (result.isConfirmed) {

            fetch(`../api/manageSprints/endTask?id=${document.getElementById('id_task').value}`, {
                method: 'GET',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json',
                    'Authorization': localStorage.token
                }
            }).then(function (request) {
                request.json().then(function (response) {
                    // checks response status
                    if (response.status) {
                        closeModal('manageTasksModal');
                        loadSprints();
                        Swal.fire('Success!', response.message, 'success');
                    } else {
                        Swal.fire('Warning!', response.exception, 'warning');
                    }
                });
            });
        }
    });
}
