function redirect(url){
    fetch(url, {
        method: 'GET',
        headers: {
            'Authorization': localStorage.token
        }
    }).then(function (request) {
        request.json(function(response){
            window.location.href = response;
        });
    })
}