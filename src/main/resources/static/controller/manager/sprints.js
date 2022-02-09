document.addEventListener('DOMContentLoaded', function () {
makeRequests();
});

async function makeRequests() {
    fillManagersSprintTable().then(function () {
        fillIssuesTable().then(function(){
            fillSelect('../api/users/ListStatus', 'id_status', null);
        });   
       // 
    
    });

}


async function fillManagersSprintTable() {
    const request = await fetch('../api/users/sprints', {
        method: 'GET',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'Authorization': localStorage.token
        }
    });

    // parses request to json
    request.json().then(function (response) {
        // checks response status
        if (response.status) {
            console.log(response)
            document.getElementById('id_sprint').value = response.dataset[0].id;
            console.log(response.dataset[0].id);
            $('#manager-table').DataTable().destroy();
            let content = '';
            response.dataset.map(function (row) {
                content += `
                <tr>
                    <th scope="row" class="id-padding">${row[0]}</th>
                    <td>${row[1]}</td>
                    <td>${row[2]}</td>
                    <td>${row[3]}</td>
                    <td>${row[4]}</td>
                    <td>${row[5]}</td>
                    <td>${row[6]}</td>
                    <th scope="row">
                    <div>
                    <a onclick="getOneSprint(${row[0]})" class="btn btn-sm custom-btn" data-bs-toggle="tooltip" data-bs-placement="top" title="Update"><i class="bi bi-pencil-fill"></i></a>
                    <a onclick="deleteSprint(${row[0]})" class="btn btn-sm custom-btn"  data-bs-toggle="tooltip" data-bs-placement="top" title="Delete"><i class="bi bi-trash2-fill"></i></a>
                    <a onclick="fillIssuesTable(${row[0]})" class="btn btn-sm custom-btn"  data-bs-toggle="tooltip" data-bs-placement="top" title="Add"><i class="bi bi-caret-down-square-fill"></i></a>
                `
               
                if (row[6] == 'Active') {
                    content += `
                                <a onclick="disableUser(${row[0]})" class="btn btn-sm custom-btn"  data-bs-toggle="tooltip" data-bs-placement="top" title="Set inactive"><i class="bi bi-x"></i></a>
                            </div>
                        </th>
                    </tr>   
                    `
                } else if (row[6] == 'Inactive') {
                    content += `
                                <a onclick="enableUser(${row[0]})" class="btn btn-sm custom-btn"  data-bs-toggle="tooltip" data-bs-placement="top" title="Set active"><i class="bi bi bi-check"></i></a>
                            </div>
                        </th>
                    </tr>   
                    `
                }
            });
 
             document.getElementById('tbody-managers').innerHTML = content;
             $('#manager-table').DataTable();
        } else {
            Swal.fire('Warning!', response.exception, 'warning');
        }
    });
}


async function fillIssuesTable(idIssue) {
    const request = await fetch('../api/users/getAvailableIssues', {
        method: 'GET',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'Authorization': localStorage.token
        }
    });
    
    // parses request to json
    request.json().then(function (response) {
        // checks response status

        if (response.status) {
            document.getElementById("id_sprint2").value = idIssue;
            let content = '';
            
            $('#issues-table').DataTable().destroy();
            $('#issues-table-selected').DataTable().destroy();
            response.dataset.map(function (row) {
                content += `
                <tr>
                    <th scope="row" class="id-padding">${row[0]}</th>
                    <td>${row[1]}</td>
                    <th scope="row">
                        <div>
                            <a onclick="selectIssue(${row[0]}, '${row[1]}')" class="btn btn-sm custom-btn" data-bs-toggle="tooltip" data-bs-placement="top" title="Update"><i class="bi bi-plus"></i></a>
                        </div>
                    </th>
                </tr>                                                                                                                   
                `
            });

            document.getElementById('tbody-issues').innerHTML = content;
            document.getElementById('tbody-issues-selected').innerHTML = '';
            $('#issues-table').DataTable({
                "bLengthChange": false,
                "bFilter": true,
                "bInfo": false,
                "bAutoWidth": false
            });

            $('#issues-table-selected').DataTable({
                "bLengthChange": false,
                "bFilter": true,
                "bInfo": false,
                "bAutoWidth": false
            });
        } else {
            Swal.fire('Warning!', response.exception, 'warning');
        }
        
    });
    
}


function selectIssue(id, name) {
 
    if (!!document.getElementById(`row-${id}`)) {
        Swal.fire('Error!', "You can't add the same issue two times.", 'error');
    } else {
        $('#issues-table-selected').DataTable().destroy();
        let content = `
            <tr id="row-${id}">
                <th scope="row" class="id-padding">${id}</th>
                <td>${name}</td>
                <input type="hidden" class="issue-selected" value=${id}>
                <th scope="row">
                    <div>
                        <a onclick="deleteFromSelected(${id})" class="btn btn-sm custom-btn" data-bs-toggle="tooltip" data-bs-placement="top" title="Update"><i class="bi bi-x"></i></a>
                    </div>
                </th>
            </tr>  
            `

        
        document.getElementById('tbody-issues-selected').innerHTML += content;
        $('#issues-table-selected').DataTable({
            "bLengthChange": false,
            "bFilter": true,
            "bInfo": false,
            "bAutoWidth": false
        });
    }
}

function deleteFromSelected(id) {
    $('#issues-table-selected').DataTable().destroy();
    document.getElementById(`row-${id}`).remove();
    $('#issues-table-selected').DataTable({
        "bLengthChange": false,
        "bFilter": true,
        "bInfo": false,
        "bAutoWidth": false
    });
}

function getSelectedValues() {
    var x = document.getElementsByClassName("issue-selected");
    var data = [];
    for (i = 0; i < x.length; i++) {
        data.push(x.item(i).value);
    }

    return data;
}

document.getElementById('btnAddIssues').addEventListener('click', function () {
    saveIssues();
});

async function getOneSprint(id) {
    const request = await fetch(`../api/users/sprint?id=${id}`, {
        method: 'GET',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'Authorization': localStorage.token
        }
    });

    request.json().then(function (response) {
        if (response.status) {
            console.log(response);
            document.getElementById('id').value = response.dataset[0].id;
            document.getElementById('name').value = response.dataset[0].cycle_name;
            document.getElementById('duration').value = response.dataset[0].duration;
            document.getElementById('start_date').value = response.dataset[0].start_date;
            document.getElementById('end_date').value = response.dataset[0].end_date;
            document.getElementById('description').value = response.dataset[0].description;
            fillSelect('../api/users/ListStatus', 'id_status', response.dataset[0].id_status);

            document.getElementById('modal-title').textContent = 'Update sprint';

            openModal('manageSprintsModal');

        } else {
            Swal.fire('Error!', response.exception, 'error')
        }
    });
}

function deleteSprint(id) {
    Swal.fire({
        title: 'Delete',
        text: "Are you sure that you want to delete this register?",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Yes, delete it.'
    }).then((result) => {
        if (result.isConfirmed) {
            fetch(`../api/users/deleteSprint?id=${id}`, {
                method: 'DELETE',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json',
                    'Authorization': localStorage.token
                }
            }).then(function (request) {
                request.json().then(function (response) {
                    // checks response status
                    if (response.status) {
                        makeRequests();
                        Swal.fire('Success!', response.message, 'success');
                    } else {
                        Swal.fire('Warning!', response.exception, 'warning');
                    }
                });
            });
        }
    });
}

async function saveIssues() {

    let data = {};
            data.id = document.getElementById('id_sprint2').value;
            console.log(data);
 /*  if (getSelectedValues().length != 0) {
        data = {};
        data.id_project = id;
        data.developers = getSelectedValues();
        if (id != 0) {
            const request = await fetch('../api/projects/addDevelopersToProject', {
                method: 'POST',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json',
                    'Authorization': localStorage.token
                },
                body: JSON.stringify(data)
            });

            request.json().then(function (response) {
                if (response.status) {
                    closeModal('addUsersProject');
                    Swal.fire("Success!", response.message, 'success').then(function () {
                        fillProjects().then(function () {
                            fillDevelopersTable();
                        });
                    });
                } else {
                    Swal.fire('Warning!', response.exception, 'warning').then(function () {
                        fetch(`../api/projects/delete?id=${id}`, {
                            method: 'DELETE',
                            headers: {
                                'Accept': 'application/json',
                                'Content-Type': 'application/json',
                                'Authorization': localStorage.token
                            }
                        }).then(function (request) {
                            request.json().then(function (response) {
                                if (response.status) {
                                    console.log(response.message);
                                } else {
                                    console.log(response.exception);
                                }
                            });
                        });
                    });
                }
            });
        } else {
            Swal.fire("Error!", `We have an error, the id of the project is ${id}.`, 'error');
        }
    } else {
        console.log(id);
        Swal.fire('Warning!', "You need to select developers for the project.", 'warning').then(function () {
            fetch(`../api/projects/delete?id=${id}`, {
                method: 'DELETE',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json',
                    'Authorization': localStorage.token
                }
            }).then(function (request) {
                request.json().then(function (response) {
                    if (response.status) {
                        console.log(response.message);
                    } else {
                        console.log(response.exception);
                    }
                });
            });
        });

    }*/
}

    // resets the form when the add button is pressed
    document.getElementById('btnOpenAddModal').addEventListener('click', function () {
        document.getElementById('manageSprints-form').reset();
        document.getElementById('modal-title').textContent = 'Add sprints';
    });

    // to click the hidden button into the manageUsers-form
    function clickHiddenButton() {
        document.getElementById('submit-form').click();
    }

    document.getElementById('manageSprints-form').addEventListener('submit', function (event) {
        //prevents to reload the page
        event.preventDefault();

            let data = {};
        
            data.cycle_name = document.getElementById('name').value;
            data.duration = document.getElementById('duration').value;
            data.start_date = document.getElementById('start_date').value;
            data.end_date = document.getElementById('end_date').value;
            data.description = document.getElementById('description').value;
            data.id_status = document.getElementById('id_status').value;


            if (document.getElementById('id_sprint').value != '') {
                data.id = document.getElementById('id_sprint').value;
                saveOrUpdateData('../api/users/updateSprint', data, 'manageSprintsModal').then(function () {
                    makeRequests();
                });
            } else {
                saveOrUpdateData('../api/users/createSprint', data, 'manageSprintsModal').then(function () {
                    makeRequests();
                });
            }


    });
           
