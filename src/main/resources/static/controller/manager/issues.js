document.addEventListener('DOMContentLoaded', function () {
    makeRequests();

});

async function makeRequests() {
    fillIssuesTable().then(function () {
        fillSelect('../api/issues/priorities', 'id_priority', null).then(function () {
            fillSelect('../api/projects/getProjects', 'id_project', null).then(function () {
                fillSelect('../api/issues/ListIssueStatus', 'id_status', null).then(function () {
                    fillSelect('../api/issues/types', 'id_type', null).then(function () {
                        fillSelect('../api/issues/scores', 'id_score', null).then(function () {
                            fillSelect('../api/users/sprints', 'id_development_cycle', null).then(function () {
                            });
                        });
                    });
                });
            });
        });
    });
}


async function fillIssuesTable() {
    const request = await fetch('../api/issues/issues', {
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
            document.getElementById('id_issue').value = response.dataset[0].id;
            $('#manager-table').DataTable().destroy();
            let content = '';
            console.log(response.dataset);
            response.dataset.map(function (row) {
                content += `
                <tr>
                    <th scope="row" class="id-padding">${row[0]}</th>
                    <td>${row[1]}</td>
                    <td>${row[2]}</td>
                    <td>${row[3]}</td>
                    <td>${row[4]}</td>
                    <td>${row[5]}</td>
                    <th scope="row">
                        <div>
                            <a onclick="getOneIssue(${row[0]})" class="btn btn-sm custom-btn" data-bs-toggle="tooltip" data-bs-placement="top" title="Update"><i class="bi bi-pencil-fill"></i></a>
                            <a onclick="deleteIssues(${row[0]})" class="btn btn-sm custom-btn"  data-bs-toggle="tooltip" data-bs-placement="top" title="Delete"><i class="bi bi-trash2-fill"></i></a>
                        </div>
                </th>
            </tr>                                                                                                                        
                `
            });

            document.getElementById('tbody-managers').innerHTML = content;
            $('#manager-table').DataTable();
        } else {
            Swal.fire('Warning!', response.exception, 'warning');
        }
    });
}

async function getOneIssue(id) {
    const request = await fetch(`../api/issues/issue?id=${id}`, {
        method: 'GET',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'Authorization': localStorage.token
        }
    });

    request.json().then(function (response) {
        console.log(response.dataset);
        if (response.status) {
            console.log(response)
            document.getElementById('id_issue').value = response.dataset[0].id;
            document.getElementById('summary').value = response.dataset[0].summary;
            document.getElementById('description').value = response.dataset[0].description;
            document.getElementById('created_by').value = response.dataset[0].created_by;
            fillSelect('../api/issues/priorities', 'id_priority', response.dataset[0].id_priority).then(function () {
                fillSelect('../api/projects/getProjects', 'id_project', response.dataset[0].id_project).then(function () {
                    fillSelect('../api/issues/ListIssueStatus', 'id_status', response.dataset[0].id_status).then(function () {
                        fillSelect('../api/issues/types', 'id_type', response.dataset[0].id_type).then(function () {
                            fillSelect('../api/issues/scores', 'id_score', response.dataset[0].id_score).then(function () {
                                fillSelect('../api/users/sprints', 'id_development_cycle', response.dataset[0].id_development_cycle).then(function () {
                                });
                            });
                        });
                    });
                });
            });

            document.getElementById('modal-title').textContent = 'Update issue';

            openModal('manageIssuesModal');

        } else {
            Swal.fire('Error!', response.exception, 'error')
        }
    });
}


function deleteIssues(id) {
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
            fetch(`../api/issues/delete?id=${id}`, {
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

// resets the form when the add button is pressed
document.getElementById('btnOpenAddModal').addEventListener('click', function () {
    document.getElementById('manageIssues-form').reset();
    document.getElementById('modal-title').textContent = 'Add Issues';
});

// to click the hidden button into the manageIssues-form
function clickIHiddenButton() {
    document.getElementById('submit-form').click();
}

document.getElementById('manageIssues-form').addEventListener('submit', function (event) {
    //prevents to reload the page
    event.preventDefault();

    let data = {};
    data.summary = document.getElementById('summary').value;
    data.description = document.getElementById('description').value;
    //document.getElementById('created_by').value = localStorage.userid;
    //data.issue_owner = document.getElementById('issue_owner').value;
    data.id_priority = document.getElementById('id_priority').value;
    data.id_project = document.getElementById('id_project').value;
    data.id_status = document.getElementById('id_status').value;
    data.id_type = document.getElementById('id_type').value;
    data.id_score = document.getElementById('id_score').value;
    data.id_development_cycle = document.getElementById('id_development_cycle').value;

    if (document.getElementById('id_issue').value != 'undefined') {
        data.id = document.getElementById('id_issue').value;
        data.created_by = document.getElementById('created_by').value;
        console.log("Here " + data.id + " " + data.created_by);
        saveOrUpdateData('../api/issues/update', data, 'manageIssuesModal').then(function () {
            console.log("Status " + data.id_status + " Type " + data.id_type );
            makeRequests();
        });
    } else {
        data.created_by = localStorage.userid;
        console.log("created by  " + data.created_by);
        saveOrUpdateData('../api/issues/create', data, 'manageIssuesModal').then(function () {
            makeRequests();
        });
    }


});