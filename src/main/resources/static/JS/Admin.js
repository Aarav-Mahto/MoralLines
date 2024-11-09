function btnNavigate(view) {
	var token = $('meta[name="_csrf"]').attr('content');
    var header = $('meta[name="_csrf_header"]').attr('content');
    $.ajax({
        url: '/btnNavigateAjax', 
        type: 'POST', 
        data: {view: view},
        beforeSend: function(xhr){
			xhr.setRequestHeader(header,token);
		},
        success: function(response) {
            window.location.href="/dashboard";
        },
        error: function(xhr, status, error) {
            console.error('Error:', error);
        }
    });
}

////////////////////////////////////////////////////////////////////
$(window).on('load', function() {
	var header = $('meta[name="_csrf"]').attr('content');
	var token = $('meta[name="_csrf_header"]').attr('content');
    $.ajax({
        url: '/indexHeader',
        type: 'GET',
        beforeSend: function(xhr){
			xhr.setRequestHeader(header, token);
		},
        success: function(responseHTML) {
            var category = extractDataFromHTML(responseHTML);
            $('.select-category').each(function() {
                var selectElement = $(this);
                var selectedValue = selectElement.val(); 
                selectElement.append('<option value="" selected>Choose a Option</option>');
                $.each(category, function(index, item) {
                    var option = $('<option>', {
                        value: item.value,
                        text: item.text
                    });
                    selectElement.append(option);
                });
                // Reselect the previously selected option
                selectElement.val(selectedValue);
            });
        },
        error: function(xhr, status, error) {
            console.error("Error loading index header:", status, error);
        }
    });
});

function extractDataFromHTML(html) {
    var category = [];
    var tempDiv = $('<div>').html(html);

    tempDiv.find('.tab-list li a').each(function() {
        //category.push($(this).text());
	var value = $(this).attr('value');
        var text = $(this).text();
        category.push({ value: value, text: text });
    });
    return category;
}
//====================Show Database =====================
function showDatabase(event) {
	var token = $('meta[name="_csrf"]').attr('content');
    var header = $('meta[name="_csrf_header"]').attr('content');
    $.ajax({
        url: '/handleShowDB', 
        type: 'POST', 
        beforeSend: function(xhr){
			xhr.setRequestHeader(header,token);
		},
        success: function(response) {
			//alert(response)
            window.location.href=response;
        },
        error: function(xhr, status, error) {
            console.error('Error:', error);
        }
    });
}
//custom alert
var dialogInstance;
function customAlert(dialogContent){
	if (dialogInstance && dialogInstance.dialog("isOpen")) {
        dialogInstance.dialog("close");
    }
    
	dialogInstance = dialogContent.dialog({
        title: "Full Story", // Set the dialog title
        modal: true,           // Make the dialog modal
        resizable: false,      // Disable resizing
        position: {
            my: 'center top',      // Dialog's alignment point
            at: 'center top+50',   // Alignment point of the target (center-top position with 50px margin-top)
            of: window             // Element to position against (in this case, the window)
        },
        buttons: {
            "✖": function() {
                $(this).dialog("close"); // Close the dialog when OK button is clicked
            }
        }
    });
}

function showStory(sid){
	var token = $('meta[name="_csrf"]').attr('content');
    var header = $('meta[name="_csrf_header"]').attr('content');
	
    var dialogContent = $("<div>Loading...</div>");
    $.ajax({
		url:'/showStory',
		type: 'POST',
		data: {sid: sid},
		beforeSend: function(xhr){
			xhr.setRequestHeader(header,token);
		},
		success: function(response){
			dialogContent.html("<div>"+response+"</div>");
			customAlert(dialogContent)
		},
		error: function(xhr, status, error){
			console.error("Error : ",error);
			dialogContent.html("<div>Error loading contact details.</div>");
			customAlert(dialogContent)
		}
	})
}


window.onload = function() {
    if (window.history && window.history.pushState) {
        window.history.pushState('forward', null, window.location.href);
        window.onpopstate = function(event) {
            var userConfirmed = confirm("Are you sure you want to logout?");
            if (userConfirmed) {
                window.location.href = "/logout";
            } else {
                window.history.pushState('forward', null, window.location.href);
            }
        };
    }
}

//==================View Database By Category ================================
$(document).ready(function() {
    $('#view-database-by-category').on('change', function() {
        var selectedValue = $(this).val();
        //alert('Selected value: '+selectedValue);
        var token = $('meta[name="_csrf"]').attr('content');
	    var header = $('meta[name="_csrf_header"]').attr('content');
		$.ajax({
		      url: '/selectStoryByCategory', 
		      type: 'POST', 
		      data: {category: selectedValue},
		      beforeSend: function(xhr) {
		            xhr.setRequestHeader(header, token);
		      },
		      success: function(response) {
				  updateTable(response);
		      },
		      error: function(xhr, status, error) {
		          console.error('Error:', error);
		      }
		});
    });
});

function updateTable(response){
    var tableBody = $('#table_body');
    if($.isEmptyObject(response)){
		alert("No Item Founds in Selected Category!")
    }
    else{
		tableBody.empty();
		var i=1;
        $.each(response, function(key, val){
			var storyDescForSearch = val.storyDescForSearch;
            var items='';
            items +='<tr>';
            items += '<td>'+ i +'</td>';
            items += '<td>'+ val.storyTitle +'</td>';
            items += '<td>'+ val.storyDesc +'</td>';
            items += '<td>'+ val.storyTitle +'</td>';
            items += '<td><ul><li onclick="showStory(\'' + val.storyId + '\')">Story</li></ul></td>'; 
            items += '<td>'+ val.imgName +'</td>';
            items += '<td>'+ val.category +'</td>';
            items += '<td>'+ val.entryDate +'</td>';
            items += '<td>'+ val.enteredBy +'</td>';
            items += '<td>'+ val.updatedDate +'</td>';
            items += '<td>'+ val.updatedBy +'</td>';
            items +='</tr>';
            tableBody.append(items);
            i=i+1;
        });
    }
}
