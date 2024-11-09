$(document).ready(function(){
	var colors = [
        'rgb(143, 6, 132)','rgb(152, 4, 90)','rgb(152, 4, 9)','rgb(4, 4, 152)','rgb(2, 81, 114)',
        'rgb(3, 155, 5)','rgb(155, 71, 3)','rgb(1, 52, 102)','rgb(79, 4, 59)','rgb(9, 70, 73)','rgb(2, 69, 58)','rgb(2, 69, 3)'
    ];
	var randomColor = colors[Math.floor(Math.random() * colors.length)];
    $('.call-logo').css('background-color', randomColor);
    $('.number-field div a').css('color','white');
	/*
	function isNumber(str) {
	    //str = str.replace(/[+\s-]+/g, '');
	    str = str.replace(/[(+\s-)]+/g, '');
	    return !isNaN(str);
	}
    
    $('.contact-num').each(function() {
        var text = $(this).text();
        if (isNumber(text)) {
            $(this).closest('.number-field').find('.material-symbols-outlined').text('call');
        } else {
            $(this).closest('.number-field').find('.material-symbols-outlined').text('mail');
        }
    });
    */
 
})


//=============Loading Spinner ==================================
$(window).on('load', function() {
    $('#loading-spinner').fadeOut(function(){
		$('#content').addClass('content-animate');
	});
});





//================================ Comments & Feedback=======================================================

function sentMail(event) {
    event.preventDefault();
	$("#sending").text("Sending...").css('color', 'green');
	
	var currentUrl = window.location.href;
	var url = new URL(currentUrl);
	var pathParts = url.pathname.split('/');
	var wabPageIdOnly  = pathParts[pathParts.length - 1];

    let emailId = document.getElementById("mail_id").value;
    let message = document.getElementById("message").value;

    let emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (wabPageIdOnly !== '' && emailPattern.test(emailId) && message !== '') {
        let params = {
			pageIdOnly:wabPageIdOnly,
            email_id: emailId,
            message: message
        };
        emailjs.send("service_2jnmm6w", "template_ou9hsny", params)
            .then(function (response) {
				$("#sending").text("Sent Successfully! Thanks We will get back to you soon.").css('color', 'darkgreen');
				$("#mail_id").val("");
				$("#message").val("");
            })
            .catch(function (error) {
				$("#sending").text("Failled!").css('color', 'darkred');
            });
    } else {
		$("#sending").text("All fields and a valid email address are mandatory!").css('color', '#f95c11');
    }
}
//--------------------------- Page Loading ----------------------------------------------	
window.addEventListener('load', function() {
    var loadingOverlay = document.getElementById('loadingOverlay');
    loadingOverlay.style.display = 'none';
});

  

// Moving Copy Text with cursor =================================

$(document).ready(function(){

//--------------------------- Copy Number ======================================

    $('.number-field').on('click', function() {
		$('#popup').empty();
        const textToCopy = $(this).find('a').text();
        navigator.clipboard.writeText(textToCopy).then(function() {
            $('#popup').html("<span class='c-t'>Copied Text!</span><br><label class='copied'>"+textToCopy+"</label>");
            $('#popup').css('display','block');
            setTimeout(function(){
                $('#popup').css('display','none')
            }, 3000);
        }).catch(function(err) {
            console.error('Error copying text: ', err);
        });
        $('#popup').empty();
    });

    // ============================= Effects On Element =================

    function isElementInViewport(el) {
        const rect = el[0].getBoundingClientRect();
        return (
            rect.top < (window.innerHeight || document.documentElement.clientHeight) &&
            rect.bottom > 0
        );
    }
    function checkVisibility() {
        $('.hide-boxes, .show-box-from-bottom').each(function() {
            if (isElementInViewport($(this))) {
                $(this).addClass('animate-boxes');
            } else {
                $(this).removeClass('animate-boxes'); // Remove class if not in viewport
            }
        });
    }    
    $(window).on('scroll', checkVisibility);
    checkVisibility();

});