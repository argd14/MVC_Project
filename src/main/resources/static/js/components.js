function sweetAlert(type, message, action){
    switch(type){
        case 1:
            Swal.fire(
                'Success!',
                message,
                'success'
            )
            break;
        case 2:
            Swal.fire(
                'Error!',
                message,
                'error'
            )
            break;
        case 3:
            Swal.fire(
                'Warning!',
                message,
                'warning'
            )
            break;
    }
}