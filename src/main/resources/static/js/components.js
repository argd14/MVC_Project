function redirect(url) {
    window.location.href = `${url}?token=${localStorage.token}`;
}

function saveOrUpdate(endpoint, data, modal, action){
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
                Swal.fire('Success!',response.message,'success').then(function (){
                    action();
                });
            } else {
                Swal.fire('Warning!',response.exception,'warning');
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
            ).then(function(){
                window.location.href = "/";
            });
        }
    })
}