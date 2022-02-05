document.addEventListener('DOMContentLoaded',function(){
    
});

document.getElementById('login-form').addEventListener('submit',function(event){
    event.preventDefault();

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
        // parses request to json
        request.json().then(function (response) {
            console.log(response);
            // checks response status
            if (response.status) {
                Swal.fire('Success!',response.message,'success').then(function (){
                    localStorage.token = response.token;
                    
                    if(response.dataset[0].id_rol == 3){
                        window.location.href = `developer/dashboard?token=${response.token}`;
                    } else if (response.dataset[0].id_rol == 1){
                        window.location.href = `manager/dashboard?token=${response.token}`;
                    }

                });
            } else {
                Swal.fire('Warning!',response.exception,'warning');
            }
        });
    }).catch(function (error) {
        console.log(error);
    });
});