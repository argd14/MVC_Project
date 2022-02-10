document.addEventListener('DOMContentLoaded',function(){
    loadProjectData();
});

async function loadProjectData(){
    const request = await fetch(`../api/projects/getProjectDetails?id=${document.getElementById('id_project').value}`,{
        method: 'GET',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'Authorization': localStorage.token
        }
    });

    request.json().then(function(response){
        if (response.status) {
            document.getElementById('project_name').textContent = response.dataset[0][2];
            document.getElementById('description').textContent = response.dataset[0][3];
        } else {
            Swal.fire('Error!',response.exception, 'error');
        }
    });
}