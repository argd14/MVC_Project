document.addEventListener('DOMContentLoaded', function () {
    makeRequests();
});

async function makeRequests() {
    getLoggedUser().then(function () {
        });
}

async function getLoggedUser() {
    const request = await fetch(`../api/users/getLoggedUser`, {
        method: 'GET',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'Authorization': localStorage.token
        }
    });

   request.json().then(function (response) {
        if (response.status) {
            console.log(response)
            document.getElementById('id_user').value = response.dataset[0][0];
            document.getElementById('name').value = response.dataset[0][1];
            document.getElementById('userName').value = response.dataset[0][2];
            document.getElementById('phone_number').value = response.dataset[0][3];
            document.getElementById('email').value = response.dataset[0][4];
        } else {
            Swal.fire('Error!', response.exception, 'error')
        }
    });
}

document.getElementById('manageAccount-form').addEventListener('submit', function (event) {
    event.preventDefault();
    data = {}
    data.id = document.getElementById('id_user').value;
    data.name = document.getElementById('name').value;
    data.userName = document.getElementById('userName').value;
    data.phone_number = document.getElementById('phone_number').value;
    data.email = document.getElementById('email').value;

    fetch('../api/users/updateProfile', {
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
//                closeModal('createProjectModal');
                Swal.fire("Success!", response.message, 'success').then(function () {
                    localStorage.username = document.getElementById('userName').value;
                    redirect('settings');
                });
            } else {
                Swal.fire("Warning!", response.exception, 'warning');
            }
        });
    })
});

function showHidePassword(checkbox, pass1, pass2, pass3) {
    var check = document.getElementById(checkbox);
    var password1 = document.getElementById(pass1);
    var password2 = document.getElementById(pass2);
    var password3 = document.getElementById(pass3);

    if (check.checked == true) {
        password1.type = 'text';
        password2.type = 'text';
        password3.type = 'text';
    } else {
        password1.type = 'password';
        password2.type = 'password';
        password3.type = 'password';
    }
}