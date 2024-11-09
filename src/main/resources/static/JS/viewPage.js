$(document).ready(function(){
    var currentUrl = window.location.href;
    var spliting = currentUrl.split('/');
    var storyId = spliting.pop();
    
    var token = $('meta[name="_csrf"]').attr('content');
    var header = $('meta[name="_csrf_header"]').attr('content');
    $.ajax({
		url: '/hindi-story/loadMainContent',
		type: 'POST',
		data: {storyId: storyId},
		beforeSend: function(xhr) {
            xhr.setRequestHeader(header, token);
        },
		success: function(response){
			if(response == null){
				window.location.href="/hindi-story";
			}
			else{
				setMainContent(response);
				loadRecommend(response.category)
			}
		},
		error: function(xhr, status, error){
			console.error("Error: ",error);
		}
	})
})

function setMainContent(response){
	$('#loading-spinner').hide();
	$("title").text(response.storyTitle);
	$('meta[name="description"]').attr("content", response.storyDesc);
	$('.title .story-title').html(response.storyTitle);
	$('.title .story-desc').html(response.storyDesc);
	$('#category_name').text(response.category);
	var fullStory = response.story;
	var $fullStory = $('<div>' + fullStory + '</div>');
	var $noOfParagraph = $fullStory.find('p');
	var $pragraph = $('.paragraph');
	$pragraph.empty();
	if($noOfParagraph.length === 0){
		var imagePath = response.imgFullPath;
		$pragraph.append('<div class="image1"><img src="'+imagePath+'" alt="Image" loading="lazy"></div>');
		$pragraph.append('<p>'+fullStory+'</p>')
		//alert("setting Content if");
	}
	///Image/img1.jpg
	else{
		var i=1;
		var imagePath = response.imgFullPath;
		$fullStory.find('p').each(function() {
		    var paragraphText = $(this).text();
		    //alert("setting Content else");
		    if (i % 2 != 0) {
		        $pragraph.append('<div class="image1"><img src="' + imagePath + '" alt="Image" loading="lazy"></div>');
		    } else {
		        $pragraph.append('<div class="image2"><img src="' + imagePath + '" alt="Image" loading="lazy"></div>');
		    }
		    $pragraph.append('<p>' + paragraphText + '</p>');
		    
		    i++;
		});
		i=1;
	}
	
}









//==========Recommendation=======================
function loadRecommend(category){
	var token = $('meta[name="_csrf"]').attr('content');
    var header = $('meta[name="_csrf_header"]').attr('content');
    $.ajax({
	url: '/hindi-story/recommendation',
	type: 'POST',
	data: {category: category},
	beforeSend: function(xhr) {
        xhr.setRequestHeader(header, token);
    },
	success: function(response){
	    updateRecommended(response);
	},
    error: function(xhr, status, error){
		console.error("Error: ",error)
    }
})
}

function updateRecommended(response){
    var recommend = $('.recommendation');
    if($.isEmptyObject(response)){
		recommend.append("No More Item Available");
    }
    else{
		recommend.empty();
        $.each(response, function(key, val){
			var imagePath = val.imgFullPath;
            var items='';
            items += '<div class="suggest-box" onclick="openPageWithId(event, \'' + val.storyId + '\')">';
            items += '<div class="sugg-image"><img src="' + imagePath + '" alt="Image" loading="lazy"></div>';
            items += '<div class="sugg-overlap">';
            items += '<strong class="story-title">' + val.storyTitle + '</strong><br>';
            items += '<strong class="story-desc">' + val.storyDesc + '</strong>';
            items += '</div>';
            items += '</div>';
            recommend.append(items);
        });
    }
}

function openPageWithId(event,myId){
    event.preventDefault();
    window.location.href="/hindi-story/view/"+myId;
}


// scroll - to - top ========================================

$(window).scroll(function() {
    var scrollToTopButton = $('#scroll-to-top');
    if ($(this).scrollTop() > 200) {
        scrollToTopButton.fadeIn();
    } else {
        scrollToTopButton.fadeOut();
    }
});

$('#scroll-to-top').click(function() {
    $('html, body').animate({scrollTop: 0}, 'slow');
});

