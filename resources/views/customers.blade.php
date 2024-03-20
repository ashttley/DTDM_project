
<!doctype html>
<html lang="en">
<head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css">

    <title>Laravel RealTime CRUD Using Google Firebase</title>

</head>
<body>

<div class="container" style="margin-top: 50px;">

    <h4 class="text-center">Laravel RealTime CRUD Using Google Firebase</h4><br>

    <h5># Add Food</h5>
    <div class="card card-default">
        <div class="card-body">
            <form id="addFood" class="form" method="POST" action="">
                <div class="form-group">
                    <label for="name">Name</label>
                    <input id="name" type="text" class="form-control" name="name" placeholder="Name" required autofocus>
                </div>
                <div class="form-group">
                    <label for="price">Price</label>
                    <input id="price" type="number" class="form-control" name="price" placeholder="Price" required>
                </div>
                <div class="form-group">
                    <label for="description">Description</label>
                    <textarea id="description" class="form-control" name="description" placeholder="Description" required></textarea>
                </div>
                <div class="form-group">
                    <label for="status">Status</label>
                    <select id="status" class="form-control" name="status" required>
                        <option value="available">Available</option>
                        <option value="unavailable">Unavailable</option>
                    </select>
                </div>
                <div class="form-group">
                    <label for="image">Image URL</label>
                    <input id="image" type="text" class="form-control" name="image" placeholder="Image URL" required>
                </div>
                <button id="submitFood" type="button" class="btn btn-primary">Submit</button>
            </form>
        </div>
    </div>

    <br>

    <h5># Foods</h5>
    <table class="table table-bordered">
        <tr>
            <th>Name</th>
            <th>Price</th>
            <th>Description</th>
            <th>Status</th>
            <th>Image</th>
            <th width="180" class="text-center">Action</th>
        </tr>
        <tbody id="tbody">

        </tbody>
    </table>
</div>

<!-- Update Model -->
<form action="" method="POST" class="food-update-record-model form-horizontal">
    <div id="update-modal" data-backdrop="static" data-keyboard="false" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="custom-width-modalLabel"
         aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered" style="width:55%;">
            <div class="modal-content" style="overflow: hidden;">
                <div class="modal-header">
                    <h4 class="modal-title" id="custom-width-modalLabel">Update Food</h4>
                    <button type="button" class="close" data-dismiss="modal"
                            aria-hidden="true">
                    </button>
                </div>
                <div class="modal-body" id="updateBody">

                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-light"
                            data-dismiss="modal">Close
                    </button>
                    <button type="button" class="btn btn-success updateFood">Update
                    </button>
                </div>
            </div>
        </div>
    </div>
</form>

<!-- Delete Model -->
<form action="" method="POST" class="food-remove-record-model">
    <div id="remove-modal" data-backdrop="static" data-keyboard="false" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="custom-width-modalLabel"
         aria-hidden="true" style="display: none;">
        <div class="modal-dialog modal-dialog-centered" style="width:55%;">
            <div class="modal-content">
                <div class="modal-header">
                    <h4 class="modal-title" id="custom-width-modalLabel">Delete Food</h4>
                    <button type="button" class="close remove-data-from-delete-form" data-dismiss="modal"
                            aria-hidden="true">×
                    </button>
                </div>
                <div class="modal-body">
                    <p>Do you want to delete this food?</p>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default waves-effect remove-data-from-delete-form"
                            data-dismiss="modal">Close
                    </button>
                    <button type="button" class="btn btn-danger waves-effect waves-light deleteFood">Delete
                    </button>
                </div>
            </div>
        </div>
    </div>
</form>

<div class="text-center">
    <ul class="pagination" id="pagination"></ul>
</div>

{{--Firebase Tasks--}}
<script src="https://code.jquery.com/jquery-3.4.0.min.js"></script>
<script src="https://www.gstatic.com/firebasejs/5.10.1/firebase.js"></script>
<script>
    
    // Initialize Firebase
    var config = {
        apiKey: "{{ config('services.firebase.api_key') }}",
        authDomain: "{{ config('services.firebase.auth_domain') }}",
        databaseURL: "{{ config('services.firebase.database_url') }}",
        storageBucket: "{{ config('services.firebase.storage_bucket') }}",
    };
    firebase.initializeApp(config);
    var database = firebase.database();
    var lastIndex = 0;

    // Get Data
    firebase.database().ref('foods/').on('value', function (snapshot) {
        var value = snapshot.val();
        var htmls = [];
        $.each(value, function (index, value) {
            if (value) {
                htmls.push('<tr>\
                <td>' + value.name + '</td>\
                <td>' + value.price + '</td>\
                <td>' + value.description + '</td>\
                <td>' + value.status + '</td>\
                <td><img src="' + value.image + '" width="50px" height="50px"></td>\
                <td><button data-toggle="modal" data-target="#update-modal" class="btn btn-info updateData" data-id="' + index + '">Update</button>\
                <button data-toggle="modal" data-target="#remove-modal" class="btn btn-danger removeData" data-id="' + index + '">Delete</button></td>\
                </tr>');
            }
            lastIndex = index;
        });
        $('#tbody').html(htmls);
        $("#submitFood").removeClass('desabled');
    });
    // Add Food
$('#submitFood').on('click', function () {
    var values = $("#addFood").serializeArray();
    var name = values[0].value;
    var price = values[1].value;
    var description = values[2].value;
    var status = values[3].value;
    var image = values[4].value;
    var foodID = lastIndex + 1;
    firebase.database().ref('foods/' + foodID).set({
        name: name,
        price: price,
        description: description,
        status: status,
        image: image
    });
    // Reassign lastID value
    lastIndex = foodID;
    $("#addFood input, #addFood textarea").val("");
});

// Update Data
var updateID = 0;
$('body').on('click', '.updateData', function () {
    updateID = $(this).attr('data-id');
    firebase.database().ref('foods/' + updateID).on('value', function (snapshot) {
        var values = snapshot.val();
        var updateData = '<div class="form-group">\
            <label for="name">Name</label>\
            <input id="name" type="text" class="form-control" name="name" value="' + values.name + '" required autofocus>\
        </div>\
        <div class="form-group">\
            <label for="price">Price</label>\
            <input id="price" type="number" class="form-control" name="price" value="' + values.price + '" required>\
        </div>\
        <div class="form-group">\
            <label for="description">Description</label>\
            <textarea id="description" class="form-control" name="description" required>' + values.description + '</textarea>\
        </div>\
        <div class="form-group">\
            <label for="status">Status</label>\
            <select id="status" class="form-control" name="status" required>\
                <option value="available" ' + (values.status === "available" ? "selected" : "") + '>Available</option>\
                <option value="unavailable" ' + (values.status === "unavailable" ? "selected" : "") + '>Unavailable</option>\
            </select>\
        </div>\
        <div class="form-group">\
            <label for="image">Image URL</label>\
            <input id="image" type="text" class="form-control" name="image" value="' + values.image + '" required>\
        </div>';
        $('#updateBody').html(updateData);
    });
});
$('.updateFood').on('click', function () {
    var values = $(".food-update-record-model").serializeArray();
    var postData = {
        name: values[0].value,
        price: values[1].value,
        description: values[2].value,
        status: values[3].value,
        image: values[4].value
    };        
    var updates = {};
    updates['/foods/' + updateID] = postData;
    firebase.database().ref().update(updates);
    $("#update-modal").modal('hide');
});

// Remove Data
$("body").on('click', '.removeData', function () {
    var id = $(this).attr('data-id');
    $('body').find('.food-remove-record-model').append('<input name="id" type="hidden" value="' + id + '">');
});
$('.deleteFood').on('click', function () {
    var values = $(".food-remove-record-model").serializeArray();
    var id = values[0].value;
    firebase.database().ref('foods/' + id).remove();
    $('body').find('.food-remove-record-model').find("input").remove();
    $("#remove-modal").modal('hide');
});
$('.remove-data-from-delete-form').click(function () {
    $('body').find('.food-remove-record-model').find("input").remove();
});

// Variables for pagination
var currentPage = 1;
var recordsPerPage = 5;

// Function to display records
function displayRecords(records, startIndex) {
    var endIndex = startIndex + recordsPerPage;
    var trimmedRecords = records.slice(startIndex, endIndex);
    // Your code to display records on the UI
}

// Function to handle pagination
function setupPagination(records) {
    var totalPages = Math.ceil(records.length / recordsPerPage);
    var paginationHTML = '';

    for (var i = 1; i <= totalPages; i++) {
        paginationHTML += '<li class="page-item"><a class="page-link" href="#" data-page="' + i + '">' + i + '</a></li>';
    }

    $('#pagination').html(paginationHTML);

    // Handle click event for pagination
    $('.pagination .page-link').on('click', function(e) {
        e.preventDefault();
        currentPage = parseInt($(this).data('page'));
        var startIndex = (currentPage - 1) * recordsPerPage;
        displayRecords(records, startIndex);
    });
}

// Update pagination when data changes
firebase.database().ref('foods/').on('value', function(snapshot) {
    var value = snapshot.val();
    var records = Object.keys(value).map(function(key) {
        return value[key];
    });
    setupPagination(records);
    displayRecords(records, 0); // Display first page initially
});

</script>

<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js"></script>

</body>
</html> 