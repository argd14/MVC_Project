function redirect(url){
    fetch(url, {
        method: 'GET',
        headers: {
            'Authorization': localStorage.token
        }
    }).then(function (request) {
        console.log(request);
    })
}