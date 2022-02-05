function redirect(url) {
    window.location.href = `${url}?token=${localStorage.token}`;
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