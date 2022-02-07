document.addEventListener('DOMContentLoaded', function () {
    fillDevelopersTable();
});

document.getElementById('btnContinue').addEventListener('click',function(){
    if (document.getElementById('project_name').value == '' || 
        document.getElementById('project_code').value == '' ||
        document.getElementById('description').value == '') {
        Swal.fire('Warning!', "Please, fill all the fields.", 'warning');
    } else {
        closeModal('createProjectModal');
        openModal('addUsersProject');
    }
});

async function createProject(){
    data = {}
    
    data.project_name = document.getElementById('project_name').value;
    data.project_code = document.getElementById('project_code').value;
    data.description = document.getElementById('description').value;
    
    const request = await fetch('../api/projects/create',{
        method: 'POST',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'Authorization': localStorage.token
        },
        body: JSON.stringify(data)
    });

    request.json().then(function(response){
        if (response.status) {
            let id = response.dataset[0]; 
            saveProject(id)
        } else {
            Swal.fire('Warning!',response.exception,'warning').then(function(){
                closeModal('addUsersProject');
                openModal('createProjectModal');
            });
        }
    });
}

async function saveProject(id){
    if(getSelectedValues().length != 0){
        console.log(id);
        data = {};
        data.id_project = id;
        data.developers = getSelectedValues();
        
        if (id != 0) {
            const request = await fetch('../api/projects/addDevelopersToProject',{
                method: 'POST',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json',
                    'Authorization': localStorage.token
                },
                body: JSON.stringify(data)
            });
        
            request.json().then(function(response){
                if (response.status) {
                    closeModal('addUsersProject');
                    Swal.fire("Success!",response.message,'success');
                } else {
                    Swal.fire('Warning!',response.exception,'warning').then(function(){
                        fetch(`../api/projects/delete?id=${id}`,{
                            method: 'DELETE',
                            headers: {
                                'Accept': 'application/json',
                                'Content-Type': 'application/json',
                                'Authorization': localStorage.token
                            }
                        }).then(function(request){
                            request.json().then(function(response){
                                if (response.status) {
                                    console.log(response.message);
                                } else {
                                    console.log(response.exception);
                                }
                            });
                        });
                    });
                }
            });
        } else {
            Swal.fire("Error!",`We have an error, the id of the project is ${id}.`,'error');
        }
    } else {
        console.log(id);
        Swal.fire('Warning!',"You need to select developers for the project.",'warning').then(function(){
            fetch(`../api/projects/delete?id=${id}`,{
                method: 'DELETE',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json',
                    'Authorization': localStorage.token
                }
            }).then(function(request){
                request.json().then(function(response){
                    if (response.status) {
                        console.log(response.message);
                    } else {
                        console.log(response.exception);
                    }
                });
            });
        });
        
    }
}

async function fillDevelopersTable() {
    const request = await fetch('../api/projects/getAvailableDevelopers', {
        method: 'GET',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'Authorization': localStorage.token
        }
    });

    // parses request to json
    request.json().then(function (response) {
        // checks response status
        if (response.status) {
            let content = '';
            $('#developer-table').DataTable().destroy();
            response.dataset.map(function (row) {
                content += `
                <tr>
                    <th scope="row" class="id-padding">${row[0]}</th>
                    <td>${row[1]}</td>
                    <th scope="row">
                        <div>
                            <a onclick="selectDeveloper(${row[0]}, '${row[1]}')" class="btn btn-sm custom-btn" data-bs-toggle="tooltip" data-bs-placement="top" title="Update"><i class="bi bi-plus"></i></a>
                        </div>
                    </th>
                </tr>                                                                                                                   
                `
            });

            document.getElementById('tbody-developers').innerHTML = content;
            $('#developer-table').DataTable({
                "bLengthChange": false,
                "bFilter": true,
                "bInfo": false,
                "bAutoWidth": false
            });

            $('#developer-table-selected').DataTable({
                "bLengthChange": false,
                "bFilter": true,
                "bInfo": false,
                "bAutoWidth": false
            });
        } else {
            Swal.fire('Warning!', response.exception, 'warning');
        }
    });
}

function selectDeveloper(id, name) {
    if (!!document.getElementById(`row-${id}`)) {
        Swal.fire('Error!',"You can't add the same developer two times.", 'error');
    } else {
        $('#developer-table-selected').DataTable().destroy();
        let content = `
            <tr id="row-${id}">
                <th scope="row" class="id-padding">${id}</th>
                <td>${name}</td>
                <input type="hidden" class="developer-selected" value=${id}>
                <th scope="row">
                    <div>
                        <a onclick="deleteFromSelected(${id})" class="btn btn-sm custom-btn" data-bs-toggle="tooltip" data-bs-placement="top" title="Update"><i class="bi bi-x"></i></a>
                    </div>
                </th>
            </tr>  
            `
        document.getElementById('tbody-developers-selected').innerHTML += content;
        $('#developer-table-selected').DataTable({
            "bLengthChange": false,
            "bFilter": true,
            "bInfo": false,
            "bAutoWidth": false
        });
    }
}

function deleteFromSelected(id){
    $('#developer-table-selected').DataTable().destroy();
    document.getElementById(`row-${id}`).remove();
    $('#developer-table-selected').DataTable({
        "bLengthChange": false,
        "bFilter": true,
        "bInfo": false,
        "bAutoWidth": false
    });
}

function getSelectedValues(){
    var x = document.getElementsByClassName("developer-selected");
    var data = [];
    for (i = 0; i < x.length; i++) {
        data.push(x.item(i).value);
    }

    return data;
}