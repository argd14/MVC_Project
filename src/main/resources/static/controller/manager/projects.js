document.addEventListener('DOMContentLoaded',function(){
    fillDevelopersTable();
});

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
            response.dataset.map(function (row) {
                content += `
                <tr>
                    <th scope="row" class="id-padding">${row[0]}</th>
                    <td>${row[1]}</td>
                    <th scope="row">
                        <div>
                            <a onclick="getOneManager(${row[0]})" class="btn btn-sm custom-btn" data-bs-toggle="tooltip" data-bs-placement="top" title="Update"><i class="bi bi-pencil-fill"></i></a>
                        </div>
                    </th>
                </tr>                                                                                                                   
                `
            });

            document.getElementById('tbody-developers').innerHTML = content;
            $('#developer-table').DataTable();
        } else {
            Swal.fire('Warning!', response.exception, 'warning');
        }
    });
}