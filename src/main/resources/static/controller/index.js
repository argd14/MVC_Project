function register() {
    if (document.getElementById('txtPassword1').value == document.getElementById('txtPassword2').value) {
        let data = {}
        let name = document.getElementById('txtName').value;
        let userName = document.getElementById('txtUsername').value;
        let phoneNumber = document.getElementById('txtPhone').value;
        let email = document.getElementById('txtEmail').value;
        let password = document.getElementById('txtPassword1').value;

        fetch('api/register', {
            method: 'POST',
            body: JSON.stringify(data)
        }).then(function (request) {
            // Se verifica si la petición es correcta, de lo contrario se muestra un mensaje indicando el problema.
            if (request.ok) {
                request.json().then(function (response) {
                    // Se comprueba si la respuesta es satisfactoria, de lo contrario se muestra un mensaje con la excepción.
                    if (response.status) {
                        // Mostramos mensaje de exito
                        closeModal('verificarCodigoAuth');
                        sweetAlert(1, response.message, 'dashboard.php');
                    } else {
                        sweetAlert(4, response.exception, null);
                    }
                });
            } else {
                console.log(request.status + ' ' + request.statusText);
            }
        }).catch(function (error) {
            console.log(error);
        });
    } else {
        alert("Passwords doesn't match!");
    }
}