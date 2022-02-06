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

async function saveOrUpdateData(endpoint, data, modal) {
    const request = await fetch(endpoint, {
        method: 'POST',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'Authorization': localStorage.token
        },
        body: JSON.stringify(data)
    });

    // parses request to json
    request.json().then(function (response) {
        // checks response status
        if (response.status) {
            closeModal(modal);
            Swal.fire('Success!', response.message, 'success').then(function () {
                
            });
        } else {
            Swal.fire('Warning!', response.exception, 'warning');
        }
    });
}

async function fillSelect(endpoint, select, selected) {
    const request = await fetch(endpoint, {
        method: 'GET',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'Authorization': localStorage.token
        }
    });

    request.json().then(function(response){
        // checks response status
        if (response.status) {
            let content = '';
            let value = '';
            let text = '';
            // default option
            if (!selected) {
                content += '<option value="1" disabled selected>Choose an option...</option>';
            }

            console.log(response.dataset);

            response.dataset.forEach(function(e){
                value = e[0];
                text = e[1];
            
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

function openModal(form) {
    $(document.getElementById(form)).modal('show');
}

function closeModal(form) {
    $(document.getElementById(form)).modal('hide');
}