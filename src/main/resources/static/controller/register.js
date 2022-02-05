document.getElementById('register-form').addEventListener('submit',function(event){
    event.preventDefault();

    if (document.getElementById('txtPassword1').value == document.getElementById('txtPassword2').value) {
        let data = {}
        data.name = document.getElementById('txtName').value;
        data.userName = document.getElementById('txtUsername').value;
        data.email = document.getElementById('txtEmail').value;
        data.phone_number = document.getElementById('txtPhone').value;
        data.password = document.getElementById('txtPassword1').value;

        fetch('../api/register', {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        }).then(function (request) {
            // parses the request to json
            request.json().then(function (response) {
                // checks response status
                if (response.status) {
                    Swal.fire('Success!',response.message,'success').then(function(){
                        window.location.href = "../index.html"
                    });
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
});