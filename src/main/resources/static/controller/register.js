function register() {
    if (document.getElementById('txtPassword1').value == document.getElementById('txtPassword2').value) {
        let data = {}
        data.name = document.getElementById('txtName').value;
        data.userName = document.getElementById('txtUsername').value;
        data.phoneNumber = document.getElementById('txtPhone').value;
        data.email = document.getElementById('txtEmail').value;
        data.password = document.getElementById('txtPassword1').value;

        fetch('api/register', {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        }).then(function (request) {
            // Se verifica si la petición es correcta, de lo contrario se muestra un mensaje indicando el problema.
            request.json().then(function (response) {
                // Se comprueba si la respuesta es satisfactoria, de lo contrario se muestra un mensaje con la excepción.
                if (response.status) {
                    Swal.fire('Success!',response.message,'success');
                } else {
                    Swal.fire('Warning!',response.exception,'warning');
                }
            });
        }).catch(function (error) {
            console.log(error);
        });
    } else {
        Swal.fire('Warning!',"The passwords aren't the same.",'warning');
    }
}