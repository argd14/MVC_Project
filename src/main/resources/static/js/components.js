function redirect(url) {
    window.location.href = `${url}?token=${localStorage.token}`;
}

document.addEventListener('DOMContentLoaded', function () {
    var tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'))
    var tooltipList = tooltipTriggerList.map(function (tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl)
    });

    document.getElementById('lblUsername').textContent = localStorage.username;
});

function saveOrUpdate(endpoint, data, modal, action) {
    fetch(endpoint, {
        method: 'POST',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'Authorization': localStorage.token
        },
        body: JSON.stringify(data)
    }).then(function (request) {
        // parses request to json
        request.json().then(function (response) {
            // checks response status
            if (response.status) {
                closeModal(modal);
                Swal.fire('Success!', response.message, 'success').then(function () {
                    action();
                });
            } else {
                Swal.fire('Warning!', response.exception, 'warning');
            }
        });
    }).catch(function (error) {
        console.log(error);
    });
}

function fillSelect(endpoint, select, selected) {
    fetch(endpoint, {
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
                // default option
                if (!selected) {
                    content += '<option disabled selected>Seleccionar...</option>';
                }

                console.log(response.dataset);

                

                response.dataset.map(function (row) {
                    console.log(row[1]);
                    value = Object.values(row)[0];
                    text = Object.values(row)[1];

                    console.log(value);
                    console.log(text);
                
                    if (value != selected) {
                        content += `<option value="${value}">${text}</option>`;
                    } else {
                        content += `<option value="${value}" selected>${text}</option>`;
                    }
                });

                document.getElementById(select).innerHTML = content;
            } else {
                Swal.fire('Warning!', response.exception, 'warning');
            }
        });
    }).catch(function (error) {
        console.log(error);
    });
}

function logout() {
    Swal.fire({
        title: 'Log out',
        text: "Are you sure?",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Yes, let me log out.'
    }).then((result) => {
        if (result.isConfirmed) {
            localStorage.clear();

            Swal.fire(
                'Done!',
                "We hope we're gonna see you again!",
                'success'
            ).then(function () {
                window.location.href = "/";
            });
        }
    })
}