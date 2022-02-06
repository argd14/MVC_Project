document.addEventListener('DOMContentLoaded', function () {
    makeRequests();
});

async function makeRequests() {

    fillManagersTable().then(function () {
        fillDevelopersTable().then(function () {
            fillSelect('../api/users/ListRol', 'id_rol', null).then(function () {
                fillSelect('../api/users/ListStatus', 'id_status', null);
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
                            <a href="#" class="btn btn-sm custom-btn"><i class="bi bi-pencil-fill"></i></a>
                            <a href="#" class="btn btn-sm custom-btn"><i class="bi bi-trash2-fill"></i></a>
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
                $('#developer-table').DataTable().destroy();
                content += `
                <tr>
                    <th scope="row">${row[0]}</th>
                    <td>${row[1]}</td>
                    <td>${row[2]}</td>
                    <td>${row[3]}</td>
                    <td>${row[5]}</td>
                    <th scope="row">
                        <div>
                            <a href="#" class="btn btn-sm custom-btn"><i class="bi bi-pencil-fill"></i></a>
                            <a href="#" class="btn btn-sm custom-btn"><i class="bi bi-trash2-fill"></i></a>
                        </div>
                    </th>
                </tr>
                `
            });

            document.getElementById('tbody-developers').innerHTML = content;
            $('#developer-table').DataTable();
        } else {
            Swal.fire('Warning!', response.exception, 'warning');
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

        } else {
            saveOrUpdateData('../api/users/create', data, 'manageUsersModal').then(function () {
                makeRequests();
            });
        }
    } else {
        Swal.fire('Warning!',"The passwords aren't the same.",'warning');
    }
});