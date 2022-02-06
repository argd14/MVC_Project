document.addEventListener('DOMContentLoaded', function () {
    makeRequests();
});

async function makeRequests() {
    fillManagersTable().then(function () {
        fillDevelopersTable().then(function () {
            fillSelect('../api/users/ListRol', 'id_rol', null).then(function () {
                fillSelect('../api/users/ListStatus', 'id_status', null).then(function () {
                    var tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'))
                    var tooltipList = tooltipTriggerList.map(function (tooltipTriggerEl) {
                        return new bootstrap.Tooltip(tooltipTriggerEl)
                    });
                })
            });
        });
    });
}


async function fillManagersTable() {
    const request = await fetch('../api/users/getManagers', {
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
            $('#manager-table').DataTable().destroy();
            let content = '';
            console.log(response.dataset);
            response.dataset.map(function (row) {
                content += `
                <tr>
                    <th scope="row">${row[0]}</th>
                    <td>${row[1]}</td>
                    <td>${row[2]}</td>
                    <td>${row[3]}</td>
                    <td>${row[5]}</td>
                    <th scope="row">
                        <div>
                            <a onclick="getOneManager(${row[0]})" class="btn btn-sm custom-btn" data-bs-toggle="tooltip" data-bs-placement="top" title="Update"><i class="bi bi-pencil-fill"></i></a>
                            <a onclick="deleteUsers(${row[0]})" class="btn btn-sm custom-btn"  data-bs-toggle="tooltip" data-bs-placement="top" title="Delete"><i class="bi bi-trash2-fill"></i></a>
                                                                                                                                                
                `

                if (row[5] == 'Active') {
                    content += `
                                <a onclick="disableUser(${row[0]})" class="btn btn-sm custom-btn"  data-bs-toggle="tooltip" data-bs-placement="top" title="Set inactive"><i class="bi bi-x"></i></a>
                            </div>
                        </th>
                    </tr>   
                    `
                } else if (row[5] == 'Inactive') {
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

async function getOneManager(id) {
    const request = await fetch(`../api/users/user?id=${id}`, {
        method: 'GET',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'Authorization': localStorage.token
        }
    });

    request.json().then(function (response) {
        if (response.status) {

            document.getElementById('id_user').value = response.dataset[0].id;
            document.getElementById('name').value = response.dataset[0].name;
            document.getElementById('userName').value = response.dataset[0].userName;
            document.getElementById('phone_number').value = response.dataset[0].phone_number;
            document.getElementById('email').value = response.dataset[0].email;
            fillSelect('../api/users/ListRol', 'id_rol', response.dataset[0].id_rol).then(function () {
                fillSelect('../api/users/ListStatus', 'id_status', response.dataset[0].id_status);
            });

            document.getElementById('passwordControllers').classList.add('d-none');

            document.getElementById('modal-title').textContent = 'Update user';

            openModal('manageUsersModal');

        } else {
            Swal.fire('Error!', response.exception, 'error')
        }
    });
}


function deleteUsers(id) {
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
            fetch(`../api/users/delete?id=${id}`, {
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

async function fillDevelopersTable() {
    const request = await fetch('../api/users/getDevelopers', {
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
                <tr>
                    <th scope="row">${row[0]}</th>
                    <td>${row[1]}</td>
                    <td>${row[2]}</td>
                    <td>${row[3]}</td>
                    <td>${row[5]}</td>
                    <th scope="row">
                        <div>
                            <a onclick="getOneManager(${row[0]})" class="btn btn-sm custom-btn" data-bs-toggle="tooltip" data-bs-placement="top" title="Update"><i class="bi bi-pencil-fill"></i></a>
                            <a onclick="deleteUsers(${row[0]})" class="btn btn-sm custom-btn"  data-bs-toggle="tooltip" data-bs-placement="top" title="Delete"><i class="bi bi-trash2-fill"></i></a>
                                                                                                                                                
                `

                if (row[5] == 'Active') {
                    content += `
                                <a onclick="disableUser(${row[0]})" class="btn btn-sm custom-btn"  data-bs-toggle="tooltip" data-bs-placement="top" title="Set inactive"><i class="bi bi-x"></i></a>
                            </div>
                        </th>
                    </tr>   
                    `
                } else if (row[5] == 'Inactive') {
                    content += `
                                <a onclick="enableUser(${row[0]})" class="btn btn-sm custom-btn"  data-bs-toggle="tooltip" data-bs-placement="top" title="Set active"><i class="bi bi bi-check"></i></a>
                            </div>
                        </th>
                    </tr>   
                    `
                }


            });

            document.getElementById('tbody-developers').innerHTML = content;
            $('#developer-table').DataTable();
        } else {
            Swal.fire('Warning!', response.exception, 'warning');
        }
    });
}

function disableUser(id) {
    data = {};
    data.id = id;
    data.id_status = 2;
    Swal.fire({
        title: 'Set inactive',
        text: "Are you sure that you want to set inactive this register?",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Yes, set it.'
    }).then((result) => {
        if (result.isConfirmed) {
            fetch(`../api/users/changeStatus`, {
                method: 'POST',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json',
                    'Authorization': localStorage.token
                },
                body: JSON.stringify(data)
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

function enableUser(id) {
    data = {};
    data.id = id;
    data.id_status = 1;
    Swal.fire({
        title: 'Set active',
        text: "Are you sure that you want to set active this register?",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Yes, set it.'
    }).then((result) => {
        if (result.isConfirmed) {
            fetch(`../api/users/changeStatus`, {
                method: 'POST',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json',
                    'Authorization': localStorage.token
                },
                body: JSON.stringify(data)
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

function showHidePassword(checkbox, pass1, pass2) {
    var check = document.getElementById(checkbox);
    var password1 = document.getElementById(pass1);
    var password2 = document.getElementById(pass2);

    if (check.checked == true) {
        password1.type = 'text';
        password2.type = 'text';
    } else {
        password1.type = 'password';
        password2.type = 'password';
    }
}

// resets the form when the add button is pressed
document.getElementById('btnOpenAddModal').addEventListener('click', function () {
    document.getElementById('manageUsers-form').reset();
    document.getElementById('passwordControllers').classList.remove('d-none');
    document.getElementById('modal-title').textContent = 'Add users';
});

// to click the hidden button into the manageUsers-form
function clickHiddenButton() {
    document.getElementById('submit-form').click();
}

document.getElementById('manageUsers-form').addEventListener('submit', function (event) {
    //prevents to reload the page
    event.preventDefault();

    if (document.getElementById('password').value == document.getElementById('password2').value) {
        let data = {};
        data.name = document.getElementById('name').value;
        data.userName = document.getElementById('userName').value;
        data.phone_number = document.getElementById('phone_number').value;
        data.email = document.getElementById('email').value;
        data.id_rol = document.getElementById('id_rol').value;
        data.id_status = document.getElementById('id_status').value;
        data.password = document.getElementById('password').value;
        if (document.getElementById('id_user').value != '') {
            data.id = document.getElementById('id_user').value;
            saveOrUpdateData('../api/users/update', data, 'manageUsersModal').then(function () {
                makeRequests();
            });
        } else {
            saveOrUpdateData('../api/users/create', data, 'manageUsersModal').then(function () {
                makeRequests();
            });
        }
    } else {
        Swal.fire('Warning!', "The passwords aren't the same.", 'warning');
    }
});