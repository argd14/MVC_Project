document.addEventListener('DOMContentLoaded',function(){
    if(localStorage.token != null ){
        if (localStorage.role == 1) {
            window.location.href = `manager/dashboard?token=${localStorage.token}`;
        } else if(localStorage.role == 3) {
            window.location.href = `developer/dashboard?token=${localStorage.token}`;
        }
    }
});

function showHidePassword(checkbox, pass1) {
    var check = document.getElementById(checkbox);
    var password1 = document.getElementById(pass1);

    if (check.checked == true) {
        password1.type = 'text';
    } else {
        password1.type = 'password';
    }
}

document.getElementById('login-form').addEventListener('submit',function(event){
    event.preventDefault();

    let data = {}
    data.email = document.getElementById('txtEmail').value;
    data.password = document.getElementById('txtPassword').value;
    login(data);
    
});

async function login(data){
    const request = await fetch('api/login', {
        method: 'POST',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    });

    request.json().then(function (response) {
        // checks response status
        if (response.status) {
            Swal.fire('Success!',response.message,'success').then(function (){
                localStorage.token = response.token;
                localStorage.username = response.dataset[0].userName;
                localStorage.role = response.dataset[0].id_rol;
                
                if(response.dataset[0].id_rol == 3){
                    window.location.href = `../developer/dashboard?token=${response.token}`;
                } else if (response.dataset[0].id_rol == 1){
                    window.location.href = `manager/dashboard?token=${response.token}`;
                }

            });
        } else {
            Swal.fire('Warning!',response.exception,'warning');
        }
    });
}