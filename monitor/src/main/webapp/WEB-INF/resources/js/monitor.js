/**
 * 
 */

logClicked = false;
$(document).ready(function(){
	
  // user requested enable/disable of email notifications and photo taking
  $("#control").click(function(event){
	event.preventDefault();
	deselectAll();
    $(this).addClass("menu-item-divided pure-menu-selected active");
    
    // hide main content
    $("#homePage").hide();
    $("#homePage").attr("hidden", "true");
    $("#logText").hide();
    
    $("#controlText").removeAttr("hidden");
   
  
  });
  
  // user requested log viewer
  $("#log").click(function(event){
	loadLog();
	event.preventDefault();
	deselectAll();
    $(this).addClass("menu-item-divided pure-menu-selected active");
    
    
    
  });
  
  // home section is also default on 1st page load
  $("#home").click(function(event){
		event.preventDefault();
		deselectAll();
	    $(this).addClass("menu-item-divided pure-menu-selected active");
	    $("#homePage").show();
	    
	    $("#controlText").attr("hidden", "true");
	    $("#controlText").hide();
	 
	    window.location.pathname = "/monitor;"
	    //window.location.reload();
	  });
  
  $("#controlButton").click(executeControlButton);
  
  // select #home in menu by default
 // $("#home").click();
});

/**
 * Linked to enable / disable button (i.e. #controlButton).
 */
function executeControlButton()
{
	var buttonLabel = $(this).prop('value');
    
    // disable button for duration of ajax call
    $(this).prop('disabled', true);
	
    // fire off the request to monitor controller
    request = $.ajax({
        url: "action",
        type: "post",
        data: {'action':buttonLabel}
    });

    // callback handler that will be called on success
    request.done(function (response, textStatus, jqXHR){
        // log a message to the console
        console.log("Hooray, it worked!");
        $("#control").click();
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

function loadLog()
{
	
	var logLoadURL = window.location.pathname;
	
	if (logLoadURL.indexOf("/log") == -1)
	{
		window.location.pathname += "log";
	}
	else
		{
		window.location.reload(true);
		}
	
	 
	
    // fire off the request to monitor controller
	/*
	
	var logLoadURL = window.location.pathname;
	
	if (logLoadURL.indexOf("/log") == -1)
	{
		logLoadURL = window.location.pathname + "log";
	}
	
	
    request = $.ajax({
        url: logLoadURL,
        type: "get",
        data: {'level':"ALL"}
    });

    // callback handler that will be called on success
    request.done(function (response, textStatus, jqXHR){
        // log a message to the console
        console.log("Hooray, it worked!");
        logLoaded=true;
    	var logLoadURL = window.location.pathname;
    	
    	if (logLoadURL.indexOf("/log") == -1)
    	{
    		window.location += "log";
    	}
    	else
    		{
    		window.location.reload();
    		}
        
    });

    // callback handler that will be called on failure
    request.fail(function (jqXHR, textStatus, errorThrown){
        // log the error to the console
        console.error(
            "The following error occured: "+
            textStatus, errorThrown
        );
    });
    */
}

function deselectAll() {
	 
	$("#control").removeClass("menu-item-divided pure-menu-selected");
	$("#schedule").removeClass("menu-item-divided pure-menu-selected");
	$("#statistics").removeClass("menu-item-divided pure-menu-selected");
	$("#log").removeClass("menu-item-divided pure-menu-selected");
	$("#home").removeClass("menu-item-divided pure-menu-selected");
}