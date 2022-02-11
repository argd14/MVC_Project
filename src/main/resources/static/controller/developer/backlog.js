document.addEventListener('DOMContentLoaded',function(){
    loadProjectData().then(function(){
        loadSprints();
    });
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

async function loadSprints(){
    const request = await fetch(`../api/manageSprints/getTasksOfASprint?id=${document.getElementById('id_project').value}`,{
        method: 'GET',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'Authorization': localStorage.token
        }
    });

    request.json().then(function(response){
        if (response.status) {
            console.log(response.dataset);
            let content = '';
            response.dataset.map(function(row){
                content += `
                <div class="col-12 mb-3 p-4 animate__animated animate__fadeInUp w-75" style="border: 2px solid #E8E8E8;">
                    <h5>${row.sprint_name}</h5>
                    <table class="table table-borderless mt-3">
                        <thead>
                            <tr>
                                <th scope="col"></th>
                                <th scope="col">Type</th>
                                <th scope="col">Priority</th>
                                <th scope="col">Summary</th>
                                <th scope="col">Status</th>
                                <th scope="col">Actions</th>
                            </tr>
                        </thead>
                        <tbody id="tbody-managers">
                           `
                row.tasks.map(function(row){
                    content += `<tr>`
                    // for icon selection
                    if (row[3] == 'Bug') {
                        content+=`<th scope="row" class="id-padding"><i class="bi bi-bug h5"></i></th>
                                  <td>${row[3]}</td>`;
                    } else if (row[3] == 'New Feature'){
                        content+=`<th scope="row" class="id-padding"><i class="bi bi-bookmark-plus h5"></i></th>
                                  <td>${row[3]}</td>`;
                    } else if (row[3] == 'Improvement'){
                        content+=`<th scope="row" class="id-padding"><i class="bi bi-boxes h5"></i></th>
                                  <td>${row[3]}</td>`;
                    } else if (row[3] == 'Task') {
                        content+=`<th scope="row" class="id-padding"><i class="bi bi-journal-check h5"></i></th>
                                  <td>${row[3]}</td>`;
                    }

                    // for priority color
                    if (row[2] == 'High') {
                        content += `<td style="color: red;">${row[2]}</td>`
                    } else if(row[2] == 'Medium') {
                        content += `<td style="color: orange;">${row[2]}</td>`
                    } else if (row[2] == 'Low') {
                        content += `<td style="color: green;">${row[2]}</td>`
                    }
                    content += `   
                                    <td>${row[1]}</td>
                                    <td>${row[4]}</td>
                                    <th scope="row"><a onclick="getOneManager(${row[0]})" class="btn btn-sm custom-btn" data-bs-toggle="tooltip" data-bs-placement="top" title="Update"><i class="bi bi-eye-fill"></i></a></th>
                                </tr>`
                });
                content+= ` 
                        </tbody>
                    </table>
                </div>
                `
            });

            document.getElementById('tbody-backlog').innerHTML = content;
        } else {
            Swal.fire('Error!',response.exception, 'error').then(function(){
                redirect('dashboard');
            });
        }
    });
}