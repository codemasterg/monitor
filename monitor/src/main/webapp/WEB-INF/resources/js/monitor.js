/**
 * 
 */
$(document).ready(function(){
	
  // stop/start button to enable/disable email notifications and photo taking
  $("#control").click(function(event){
	event.preventDefault();
	deselectAll();
    $(this).addClass("menu-item-divided pure-menu-selected active");
    $(".content-subhead").hide();
    $(".pure-g").hide();
    $("#controlText").removeAttr("hidden");
  });
  
  $("#home").click(function(event){
		event.preventDefault();
		deselectAll();
	    $(this).addClass("menu-item-divided pure-menu-selected active");
	    $(".content-subhead").show();
	    $(".pure-g").show();
	    $("#controlText").attr("hidden", "true");
	  });
  
  $("#controlButton").click(executeControlFunction);
  
  // select #home in menu by default
  $("#home").click();
});

function executeControlFunction()
{
	var buttonLabel = $(this).prop('value');
    
    // disable button for duration of ajax call
    $(this).prop('disabled', true);
	
    // fire off the request to monitor controller
    request = $.ajax({
        url: window.location.pathname + "action",
        type: "post",
        data: {'action':buttonLabel}
    });

    // callback handler that will be called on success
    request.done(function (response, textStatus, jqXHR){
        // log a message to the console
        console.log("Hooray, it worked!");
    });

    // callback handler that will be called on failure
    request.fail(function (jqXHR, textStatus, errorThrown){
        // log the error to the console
        console.error(
            "The following error occured: "+
            textStatus, errorThrown
        );
    });
    
    // callback handler that will be called regardless
    // if the request failed or succeeded
    request.always(function () {
        // reenable the control button
    	$("#controlButton").prop('disabled', false);
    });
	
	// toggle button label
	if (buttonLabel == 'Disable')
	{
		$(this).prop('value', 'Enable');
	}
	else
	{
		$(this).prop('value', 'Disable');
	}
}

function deselectAll() {
	 
	$("#control").removeClass("menu-item-divided pure-menu-selected");
	$("#schedule").removeClass("menu-item-divided pure-menu-selected");
	$("#statistics").removeClass("menu-item-divided pure-menu-selected");
	$("#log").removeClass("menu-item-divided pure-menu-selected");
	$("#home").removeClass("menu-item-divided pure-menu-selected");
}