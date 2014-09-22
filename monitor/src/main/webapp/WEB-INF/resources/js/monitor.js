/**
 * Implement the behavior of index, log, and control JSPs.
 */

/**
 * Register click handlers following page load
 */
$(document).ready(function(){
	
  // user requested enable/disable of email notifications and photo taking
  $("#control").click(function(event){
	event.preventDefault();
	deselectAll();
    $(this).addClass("menu-item-divided pure-menu-selected active");
    
    requestMonitorStatus();
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
	 
	    window.location.pathname = "/monitor";
	  });
  
  $("#controlButton").click(executeControlButton);
  
  $("#resetButton").click(executeResetButton);
  
  $("#silenceButton").click(executeSilenceButton);
  $("#silenceButton").css('cursor', 'pointer');
  
});

/**
 * Redirect to fetch status page that includes control button
 */
function requestMonitorStatus()
{
	window.location.pathname = "/monitor/status";
}

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
		$("#controlText").text("Disabled.  Click to enable notifications and photos.");
	}
	else
	{
		$(this).prop('value', 'Disable');
		$("#controlText").text("Enabled.  Click to disable notifications and photos.");
	}
}


/**
 * Linked to stats reset button (i.e. #resetButton).
 */
function executeResetButton()
{
	// verify user wants to reset

	if (confirm('Are you sure?  This cannot be undone.')) {

		// disable button for duration of ajax call
		$(this).prop('disabled', true);
		
		// fire off the request to monitor controller
		request = $.ajax({
			url: "doReset",
			type: "post"
		});

		// callback handler that will be called on success
		request.done(function (response, textStatus, jqXHR){
			// log a message to the console
			console.log("Reset worked!");
			$("#resetText").text("Reset complete.");
		});

		// callback handler that will be called on failure
		request.fail(function (jqXHR, textStatus, errorThrown){
			// log the error to the console
			console.error(
					"The following error occured: "+
					textStatus, errorThrown
			);
			$("#resetText").text("Reset failed, check browser console and server log.");
		});

		// callback handler that will be called regardless
		// if the request failed or succeeded
		request.always(function () {
			// reenable the reset button
			$("#resetButton").prop('disabled', false);
		});
	}
}

/**
 * Linked to alarm silence button (i.e. #silenceButton).
 */
function executeSilenceButton()
{		
	// disable button for duration of ajax call
	$(this).prop('disabled', true);
	$(this).css("opacity", "0.3");
	
	// fire off the request to monitor controller
	request = $.ajax({
		url: "doSilence",
		type: "post"
	});

	// callback handler that will be called on success
	request.done(function (response, textStatus, jqXHR){
		// log a message to the console
		console.log("Alarm Silenced!");
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
		// reenable the silence button
		$("#silenceButton").prop('disabled', false);
		$("#silenceButton").css("opacity", "1.0");
	});
}

function loadLog()
{
	window.location.pathname = "/monitor/log";	
}

/**
 * Clear any prior selection of left side menu
 */
function deselectAll() {
	 
	$("#control").removeClass("menu-item-divided pure-menu-selected");
	$("#schedule").removeClass("menu-item-divided pure-menu-selected");
	$("#statistics").removeClass("menu-item-divided pure-menu-selected");
	$("#log").removeClass("menu-item-divided pure-menu-selected");
	$("#home").removeClass("menu-item-divided pure-menu-selected");
}