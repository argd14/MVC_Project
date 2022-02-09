document.addEventListener('DOMContentLoaded', function () {
    makeRequests();
});

function makeRequests() {
    fillProjects().then(function () {
        fillDevelopersTable();
    });
}

document.getElementById('search-form').addEventListener('submit', function (event) {
    event.preventDefault();

    fetch(`../api/projects/search?search=${document.getElementById('search').value}`, {
        method: 'GET',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'Authorization': localStorage.token
        }
    }).then(function (request) {
        // parses request to json
        request.json().then(function (response) {
            // checks response status
            if (response.status) {
                let content = '';
                if (response.dataset.length != 0) {
                    response.dataset.map(function (row) {
                        content += `
                        <div class="animate__animated animate__fadeInRight dropdown d-flex justify-content-center col-xl-3 col-md-6 col-sm-12 col-xs-12">
                            <button class="btn custom-card-active" id="dropdownMenuButton${row[0]}" data-bs-toggle="dropdown" aria-expanded="false">
                                <div class="row mb-4 pb-2 pe-3">
                                    <div class="col-12 d-flex justify-content-end flex-column align-items-end">
                                        <i class="bi bi-box h3"></i>
                                    </div>
                                </div>
                                <div class="row ps-3">
                                    <div class="col-12 d-flex justify-content-start flex-column align-items-start">
                                        <h4>${row[1]}</h3>
                                        <label>${row[2]}</label>
                                    </div>
                                </div>
                            </button>
                            <ul class="dropdown-menu animate__animated animate__fadeInUp animate__faster ms-5" aria-labelledby="dropdownMenuButton${row[0]}">
                                <li><a class="dropdown-item" onclick="manageProject(${row[0]})">Edit project details</a></li>
                                <li><a class="dropdown-item" onclick="manageDevelopers(${row[0]})">Manage developers</a></li>
                                <li><a class="dropdown-item" onclick="deleteProjectFull(${row[0]})">Delete project</a></li>
                            </ul>
                        </div>                                                                                                                   
                        `
                    });
                    document.getElementById('tbody-projects').innerHTML = content;
                } else {
                    Swal.fire("Information", "No results found.", 'info');
                }
            } else {
                Swal.fire('Warning!', response.exception, 'warning');
            }
        });
    });
});

async function manageProject(id) {
    const request = await fetch(`../api/projects/getProjectDetails?id=${id}`, {
        method: 'GET',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'Authorization': localStorage.token
        }
    });

    request.json().then(function (response) {
        if (response.status) {
            document.getElementById('btnContinue').classList.add('d-none');
            document.getElementById('btnUpdate').classList.remove('d-none');
            document.getElementById('createProjectModal-title').textContent = 'Update project details';

            document.getElementById('id_project').value = response.dataset[0][0];
            document.getElementById('project_name').value = response.dataset[0][2];
            document.getElementById('project_code').value = response.dataset[0][1];
            document.getElementById('description').value = response.dataset[0][3];
            openModal('createProjectModal');
        } else {
            Swal.fire("Error", response.exception, 'error');
        }
    });
}

document.getElementById('btnUpdate').addEventListener('click', function () {
    data = {}
    data.id = document.getElementById('id_project').value;
    data.project_name = document.getElementById('project_name').value;
    data.project_code = document.getElementById('project_code').value;
    data.description = document.getElementById('description').value;

    fetch('../api/projects/update', {
        method: 'POST',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'Authorization': localStorage.token
        },
        body: JSON.stringify(data)
    }).then(function (request) {
        request.json().then(function (response) {
            if (response.status) {
                closeModal('createProjectModal');
                Swal.fire("Success!", response.message, 'success').then(function () {
                    makeRequests();
                });
            } else {
                Swal.fire("Warning!", response.exception, 'warning');
            }
        });
    })
});

async function manageDevelopers(id) {
    const request = await fetch(`../api/projects/getDevelopersOfAProject?id=${id}`, {
        method: 'GET',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'Authorization': localStorage.token
        }
    });

    request.json().then(function (response) {
        if (response.status) {
            $('#developer-table-selected').DataTable().destroy();
            let content = '';
            response.dataset.map(function (row) {
                content += `
                <tr id="row-${row[0]}">
                    <th scope="row" class="id-padding">${row[0]}</th>
                    <td>${row[1]}</td>
                    <input type="hidden" class="developer-selected" value=${row[0]}>
                    <th scope="row">
                        <div>
                            <a onclick="deleteFromSelected(${row[0]})" class="btn btn-sm custom-btn" data-bs-toggle="tooltip" data-bs-placement="top" title="Update"><i class="bi bi-x"></i></a>
                        </div>
                    </th>
                </tr>  
            `
            });
            document.getElementById('tbody-developers-selected').innerHTML = content;
            $('#developer-table-selected').DataTable({
                "bLengthChange": false,
                "bFilter": true,
                "bInfo": false,
                "bAutoWidth": false
            });

            document.getElementById('id_project2').value = id;
            document.getElementById('text-addusers').textContent = 'Choose or remove some developers to update the team.';
            document.getElementById('btnCreateProject').classList.add('d-none');
            document.getElementById('btnUpdateProject').classList.remove('d-none');
            openModal('addUsersProject');
        } else {
            Swal.fire('Warning!', response.exception, 'warning');
        }
    });
}

document.getElementById('btnOpenAddModal').addEventListener('click', function () {
    document.getElementById('createProject-form').reset();
    document.getElementById('btnContinue').classList.remove('d-none');
    document.getElementById('btnUpdate').classList.add('d-none');
    document.getElementById('createProjectModal-title').textContent = 'Create project';
});

document.getElementById('btnContinue').addEventListener('click', function () {
    if (document.getElementById('project_name').value == '' ||
        document.getElementById('project_code').value == '' ||
        document.getElementById('description').value == '') {
        Swal.fire('Warning!', "Please, fill all the fields.", 'warning');
    } else {
        closeModal('createProjectModal');
        makeRequests();
        document.getElementById('text-addusers').textContent = 'Choose some developers for the new project.';
        document.getElementById('btnCreateProject').classList.remove('d-none');
        document.getElementById('btnUpdateProject').classList.add('d-none');
        openModal('addUsersProject');
    }
});

async function fillProjects() {
    const request = await fetch('../api/projects/getProjects', {
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
            let content = '';
            response.dataset.map(function (row) {
                content += `
                    <div class="animate__animated animate__fadeInRight dropdown d-flex justify-content-center col-xl-3 col-md-6 col-sm-12 col-xs-12">
                        <button class="btn custom-card-active" id="dropdownMenuButton${row[0]}" data-bs-toggle="dropdown" aria-expanded="false">
                            <div class="row mb-4 pb-2 pe-3">
                                <div class="col-12 d-flex justify-content-end flex-column align-items-end">
                                    <i class="bi bi-box h3"></i>
                                </div>
                            </div>
                            <div class="row ps-3">
                                <div class="col-12 d-flex justify-content-start flex-column align-items-start">
                                    <h4>${row[1]}</h3>
                                    <label>${row[2]}</label>
                                </div>
                            </div>
                        </button>
                        <ul class="dropdown-menu animate__animated animate__fadeInUp animate__faster ms-5" aria-labelledby="dropdownMenuButton${row[0]}">
                            <li><a class="dropdown-item" onclick="manageProject(${row[0]})">Edit project details</a></li>
                            <li><a class="dropdown-item" onclick="manageDevelopers(${row[0]})">Manage developers</a></li>
                            <li><a class="dropdown-item" onclick="deleteProjectFull(${row[0]})">Delete project</a></li>
                        </ul>
                    </div>                                                                                                                 
                    `
            });

            document.getElementById('tbody-projects').innerHTML = content;
        } else {
            Swal.fire('Warning!', response.exception, 'warning');
        }
    });
}

function deleteProjectFull(id) {
    Swal.fire({
        title: 'Delete',
        text: "Are you sure that you want to delete this project?",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Yes, delete it.'
    }).then((result) => {
        if (result.isConfirmed) {
            fetch(`../api/projects/delete?id=${id}`, {
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

document.getElementById('btnUpdateProject').addEventListener('click', function () {
    updateDevelopers();
});

async function updateDevelopers() {
    if (getSelectedValues().length != 0) {
        data = {};
        data.id_project = document.getElementById('id_project2').value;
        data.developers = getSelectedValues();

        console.log(JSON.stringify(data));

        if (data.id_project != null) {
            const request = await fetch('../api/projects/updateDevelopersOfAProject', {
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
                    Swal.fire('Warning!', response.exception, 'warning');
                }
            });
        } else {
            Swal.fire("Error!", `We have an error, the id of the project is ${data.id_project}.`, 'error');
        }
    } else {
        Swal.fire('Warning!', "You need to select developers for the project.", 'warning');
    }
}

document.getElementById('btnCreateProject').addEventListener('click', function () {
    createProject();
});

async function createProject() {
    data = {}
    data.project_name = document.getElementById('project_name').value;
    data.project_code = document.getElementById('project_code').value;
    data.description = document.getElementById('description').value;

    const request = await fetch('../api/projects/create', {
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
            let id = response.dataset[0];
            console.log(id);
            saveProject(id)
        } else {
            Swal.fire('Warning!', response.exception, 'warning').then(function () {
                closeModal('addUsersProject');
                openModal('createProjectModal');
            });
        }
    });
}

async function saveProject(id) {
    if (getSelectedValues().length != 0) {
        console.log(id);
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

    }
}

async function fillDevelopersTable() {
    const request = await fetch('../api/projects/getAvailableDevelopers', {
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
            let content = '';
            $('#developer-table').DataTable().destroy();
            $('#developer-table-selected').DataTable().destroy();
            response.dataset.map(function (row) {
                content += `
                <tr>
                    <th scope="row" class="id-padding">${row[0]}</th>
                    <td>${row[1]}</td>
                    <th scope="row">
                        <div>
                            <a onclick="selectDeveloper(${row[0]}, '${row[1]}')" class="btn btn-sm custom-btn" data-bs-toggle="tooltip" data-bs-placement="top" title="Update"><i class="bi bi-plus"></i></a>
                        </div>
                    </th>
                </tr>                                                                                                                   
                `
            });

            document.getElementById('tbody-developers').innerHTML = content;
            document.getElementById('tbody-developers-selected').innerHTML = '';
            $('#developer-table').DataTable({
                "bLengthChange": false,
                "bFilter": true,
                "bInfo": false,
                "bAutoWidth": false
            });

            $('#developer-table-selected').DataTable({
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

function selectDeveloper(id, name) {
    if (!!document.getElementById(`row-${id}`)) {
        Swal.fire('Error!', "You can't add the same developer two times.", 'error');
    } else {
        $('#developer-table-selected').DataTable().destroy();
        let content = `
            <tr id="row-${id}">
                <th scope="row" class="id-padding">${id}</th>
                <td>${name}</td>
                <input type="hidden" class="developer-selected" value=${id}>
                <th scope="row">
                    <div>
                        <a onclick="deleteFromSelected(${id})" class="btn btn-sm custom-btn" data-bs-toggle="tooltip" data-bs-placement="top" title="Update"><i class="bi bi-x"></i></a>
                    </div>
                </th>
            </tr>  
            `
        document.getElementById('tbody-developers-selected').innerHTML += content;
        $('#developer-table-selected').DataTable({
            "bLengthChange": false,
            "bFilter": true,
            "bInfo": false,
            "bAutoWidth": false
        });
    }
}

function deleteFromSelected(id) {
    $('#developer-table-selected').DataTable().destroy();
    document.getElementById(`row-${id}`).remove();
    $('#developer-table-selected').DataTable({
        "bLengthChange": false,
        "bFilter": true,
        "bInfo": false,
        "bAutoWidth": false
    });
}

function getSelectedValues() {
    var x = document.getElementsByClassName("developer-selected");
    var data = [];
    for (i = 0; i < x.length; i++) {
        data.push(x.item(i).value);
    }

    return data;
}