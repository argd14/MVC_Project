document.addEventListener('DOMContentLoaded',function(){
    loadProjects();
});

async function loadProjects(){
    const request = await fetch('../api/projects/getMyProjects',{
        method: 'GET',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'Authorization': localStorage.token
        }
    });

    request.json().then(function(response){
        if (response.status) {
            let content = '';

            response.dataset.map(function(row){
                content += `
                <div class="animate__animated animate__fadeInUp d-flex justify-content-center col-xl-3 col-md-6 col-sm-12 col-xs-12">
                    <button onclick="redirectWithId('backlog', ${row[0]})" class="btn custom-card-active">
                        <div class="row mb-4 pb-2 pe-3">
                            <div class="col-12 d-flex justify-content-end flex-column align-items-end">
                                <i class="bi bi-box h3"></i>
                            </div>
                        </div>
                        <div class="row ps-3">
                            <div class="col-12 d-flex justify-content-start flex-column align-items-start">
                                <h4>${row[1]}</h3>
                                <label>${row[2]}</label>
                            </div>
                        </div>
                    </button>
                </div>  
                `;
            });

            document.getElementById('tbody-projects').innerHTML = content;
        } else {
            Swal.fire('Error!',response.exception, 'error');
        }
    });
}