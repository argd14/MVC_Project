function login() {
    let data = {}
    data.email = document.getElementById('txtEmail').value;
    data.password = document.getElementById('txtPassword').value;

    fetch('api/login', {
        method: 'POST',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    }).then(function (request) {
        // Se verifica si la petición es correcta, de lo contrario se muestra un mensaje indicando el problema.
        request.json().then(function (response) {
            console.log(response);
            // Se comprueba si la respuesta es satisfactoria, de lo contrario se muestra un mensaje con la excepción.
            if (response.status) {
                Swal.fire('Success!',response.message,'success').then(function (){
                    //
                });
            } else {
                Swal.fire('Warning!',response.exception,'warning');
            }
        });
    }).catch(function (error) {
        console.log(error);
    });
}