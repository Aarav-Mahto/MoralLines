// These Script used in indexHeader page
let storedTabs=null;
var flag=true;
function navigationTabs(anchor,event,identifyTabs){
    event.preventDefault();
    var tabs = identifyTabs;
    var lastIndex = $('#lastIndex').val()
    lastIndex=0;
	
    if(storedTabs !== identifyTabs){
		flag=true;
		storedTabs = identifyTabs;
    }
    else{
		storedTabs = identifyTabs;
    }
    activateTab(anchor);//header tabs activating
    $('#listLastIndex').val(0);
	$('.story-List').empty();
    storyList();
	$('#page-body').find('.post-container').empty();
    if(storedTabs === "AllNewStory" || storedTabs === null){
		var syntheticEvent = null;
		load20RecordsByCategory(event,lastIndex);
    }
    else{
		load20RecordsByCategory(event,lastIndex);
    }
}

function load20RecordsByCategory(event, lastIndex){
	var token = $('meta[name="_csrf"]').attr('content');
    var header = $('meta[name="_csrf_header"]').attr('content');
   $.ajax({
      url: '/hindi-story/load20RecordsByCategory', 
      type: 'POST', 
      data: {category: storedTabs,startIndex: lastIndex},
      beforeSend: function(xhr) {
            xhr.setRequestHeader(header, token);
      },
      success: function(response) {
		  updateStory(event,response);
      },
      error: function(xhr, status, error) {
          console.error('Error:', error);
      }
   });
}

function updateStory(event,response){
    if(event){
	event.preventDefault();
    }
    var bodyPage = $('#page-body').find('.post-container');
    var lastIndex = $('#lastIndex');
    $('#loading-spinner').hide();
	$('.loading-spinner-below').hide();
    if($.isEmptyObject(response)){
		$('#load-more').hide();
    }
    else{
	$('#load-more').show();
        $.each(response, function(key, val){
			var imagePath = val.imgFullPath;
            var items='';
            items += '<div class="post" onclick="openPageWithId(event, \'' + val.storyId + '\')">'; 
            items += '<div class="image"><img src="'+imagePath+'" alt="Image" loading="lazy"></div>';
            items += '<div class="overlap">';
            items += '<h3>' + val.storyTitle + '</h3>';
            items += '<p>' + val.storyDesc + '</p>';
            items += '</div>';
            items += '</div>';
            bodyPage.append(items);
            lastIndex.val(key);
        });
    }
}
//\\Image\/butterfly.jpg
function openPageWithId(event,myId){
    event.preventDefault();
    flag=true;
    window.location.href="/hindi-story/view/"+myId;
}


$(document).ready(function(){
    storedTabs = "AllNewStory";
    var syntheticEvent = null;
    load20RecordsByCategory(syntheticEvent,0)
})

function loadMore(event){
    event.preventDefault();
    $('.loading-spinner-below').show();
    var endIndex = $('#lastIndex').val();
    //storedTabs = "all"
    load20RecordsByCategory(event,endIndex)
}

function loadMoreList(event){
	event.preventDefault();
	storyList();
}


//==============================================================================
// highlight Tabs
function activateTab(tab) {
	$('.tab-list li a').each(function(){
		$(this).removeClass('active').css('fontWeight','normal');
	});
    $(tab).addClass('active');
    $(tab).css('fontWeight','bold')
    $('#topic-heading').text($(tab).text());
}










//Toggle drop-down

function toggleDropdown(event) {
    event.preventDefault();
    $('.story-1').toggleClass('dropdown-on');
	
    if (!($('.story-1').hasClass('dropdown-on'))) {
        $('.story-no').css('display','none');
        $('.close-icon').css('display','none');
        $('.open-icon').css('display','block');
        window.scrollTo({
	        top: 0,
	        behavior: 'smooth'
	    });
    } else {
        $('.story-no').css('display','block');
		$('.close-icon').css('display','block');
        $('.open-icon').css('display','none');
        window.scrollTo({
	        top: 0,
	        behavior: 'smooth'
	    });
    }
    if(flag)
    	storyList();
}
//======Update Story List
function updateList(response){
    if($.isEmptyObject(response)){
		$('#load-more-list').hide();
    }
    else{
		flag=false;
		$('#load-more-list').show();
        $.each(response, function(key, val){
            var items='';
            items += '<li onclick="openInNewPage(event, \'' + val.storyId + '\')">'; 
            items += '<a href="">'+val.storyTitle+'</a>';
            items += '</li>';
            $('.story-List').append(items);
            $('#listLastIndex').val(key);
        });
    }
}
function openInNewPage(event,storyId){
	event.preventDefault();
	flag=true;
    window.location.href="/hindi-story/view/"+storyId;
}
function storyList(){
	var token = $('meta[name="_csrf"]').attr('content');
    var header = $('meta[name="_csrf_header"]').attr('content');
	let categoryValue = storedTabs;
	$.ajax({
		url: '/hindi-story/load20RecordsByCategory',
		type: 'POST',
		data: {category: categoryValue, startIndex: $('#listLastIndex').val()},
		beforeSend: function(xhr) {
            xhr.setRequestHeader(header, token);
        },
		success: function(response){
			if($('#listLastIndex').val() ==0){
				$('.story-List').empty();
				updateList(response)
			}
			else{
				updateList(response)
			}
		},
		error: function(xhr, status, error){
			console.error("Error: ",error);
		}
	})
}

//Other Toggle Drop-Down Tab
function toggleDropdown1(event){
    if (!($('.story-1').hasClass('dropdown-on'))){
        $('.story-1').addClass('dropdown-on');
        $('.close-icon').css('display','block');
        $('.open-icon').css('display','none');
        
        window.scrollTo({
	        top: 0,
	        behavior: 'smooth'
	    });
	    if(flag)
	    	storyList();
    }
}























// scroll - to - top ========================================
$(window).on('scroll', function() {
    if ($(window).scrollTop() > 50 && $(window).width() < 900) {
        $('.story-1').css({'position':'fixed',"top":"30px","zIndex":5,"background":"#F1F1EF","width":"100%"});
		$('.story-no').css('maxHeight','0vh');
    } else {
		$('.story-1').css({'position':'',"top":"","zIndex":'',"background":"","width":""});
		$('.story-no').css('maxHeight','');
    }
});


$('.search-button').on('focus',function(event){
    $('.search-box-container .search-box').css('transition','all 0.5s ease-in-out');
    $('.search-box-container').css('display','block');
    $('.showing-tab').css('display','none');
    $('.story-no').css('display','none');
    $('.story-1').css('maxHeight','100vh');
    $('.search-button').css('width','95%');
    $('.fixed-search-button').css('visibility','hidden');
    $('.search-suggession').css('display','block');
    $('.search-box-container .search-box').focus();
});
$('.search-button').click(function(event){
	event.preventDefault();
    $('.search-box-container .search-box').focus();
});

function blurHandler(event){
	event.preventDefault();
    $('.search-button').css('width','');
    $('.search-box-container').css('display','none');
    $('.showing-tab').css('display','flex');
    $('.story-no').css('display','block');
    $('.search-button').css('width','');
    $('.fixed-search-button').css('visibility','');
    $('.search-suggession').css('display','none');
    $('.story-1').css('maxHeight','');
}

$('.search-box-container .search-box').click(function(event){
	blurHandler(event);
});

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


// Search Functionality Implementation ============================

$(document).ready(function() {
    function checkWidth() {
		var isNowMoreThan1000 = $(window).width() > 900;
        if (isNowMoreThan1000) {
            $('.category').css({
                'position': 'fixed',
                'z-index': '999',
                'background': '#F0F0F0',
                'top': '40px',
                'width': '100%'
            });
			$('.story-no').css('display','block');
            $('.story-1').css('marginTop','130px');
        }
		else{
			$('.category').css({
                'position': '',
                'z-index': '',
                'background': '',
                'top': '',
                'width': ''
            });
			$('.story-no').css('display','');
			$('.story-1').css('marginTop','');
		}
    }
	checkWidth();
    
    $(window).resize(function() {
        checkWidth();
    });
	storyList();
});

