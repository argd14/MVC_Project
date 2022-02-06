document.addEventListener('DOMContentLoaded',function(){
    fillSelect('../api/users/ListStatus','id_status',null);
    fillSelect('../api/users/ListRol','id_rol',null);
});

// resets the form when the add button is pressed
document.getElementById('btnOpenAddModal').addEventListener('click',function(){
    document.getElementById('manageUsers-form').reset();
    document.getElementById('modal-title').textContent = 'Add users';
});

// to click the hidden button into the manageUsers-form
function clickHiddenButton(){
    document.getElementById('submit-form').click();
}

// when the submit event is activated in the form:
document.getElementById('manageUsers-form').addEventListener('submit',function(event){
    // avoids to reload the page
    event.preventDefault();
    // fetch the endpoint
    console.log('submitted');
})