/**
 * Implement the behavior of index, log, and control JSPs.
 */

/**
 * Register click handlers following page load
 */
$(document).ready(function(){
    
  // setup handlers to enlarge photo on click event
  var numPhotos = $('.pure-img-responsive').length;  // get number of photos on page
  for (var photoId = 1; photoId < numPhotos + 1; photoId++)
  {
	  togglePhoto(photoId)
  }
    
  $("#controlButton").click(executeControlButton);
  
  $("#resetButton").click(executeResetButton);
  
  $("#silenceButton").click(executeSilenceButton);
  $("#silenceButton").css('cursor', 'pointer');
  
  $("#panicButton").mouseover(showPanicButton);
  $("#panicButton").mouseleave(showProductIcon);
  $("#panicButton").click(executePanicButton);
  
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

function showPanicButton()
{
	$("#panicButton").attr("src", "resources/images/panic.jpg");
}

function showProductIcon()
{
	$("#panicButton").attr("src", "resources/images/rebound.jpg");
}


/**
 * Linked to panic button (i.e. #panicButton).
 */
function executePanicButton()
{
    // disable button for duration of ajax call
    $(this).prop('disabled', true);
    $(this).css("opacity", "0.3");
	
    // fire off the request to monitor controller
    request = $.ajax({
        url: "action",
        type: "post",
        data: {'action':"Panic"}
    });

    // callback handler that will be called on success
    request.done(function (response, textStatus, jqXHR){
        // log a message to the console
        console.log("Panic alarm activated.");
       
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
    	$("#panicButton").prop('disabled', false);
    	$("#panicButton").css("opacity", "1.0");
    });
	
}

// toggle photo expansion
function togglePhoto(photoId)
{
	$("#photo" + photoId).click(function(event){
		 
		$(this).css('width', function(_ , cur){
			event.preventDefault();
			if (cur === '200px')
			{
				var wid = $(window).width();
				return (wid * .55) + 'px';
			}
			else
			{
				return '200px';
			}
		});
	});
	  
	$("#photo" + photoId).click(function(event){
		  $(this).css('height', function(_ , cur){
			  event.preventDefault();
			  if (cur === '150px')
			  {
				  var hit = $(window).height();
				  $(this).removeClass("pure-img-responsive");
				  return (hit * .70) + 'px';
			  }
			  else
			  {
				  $(this).addClass("pure-img-responsive");
				  return '150px';
			  }
			  
		  });
		  reducePhotos(photoId);	// it's possible another photo is already enlarged, return others to normal size so only 1 clicked image is expanded.
	});
	
	$("#photo" + photoId).css('cursor', 'pointer');  // change to pointer on hover
}

/**
 * Set all photos to their default size and class except for the given photo ID
 * @param photoId
 */
function reducePhotos(photoId)
{
	var numPhotos = $('.pure-img-responsive').length + 2;  // get number of photos on page, add 2 to cover (up to) 2 already selected item
	for (var i = 1; i < numPhotos + 1; i++)
	{
		if (i != photoId)
		{
			$("#photo" + i).css('width', "200px");
			$("#photo" + i).css('height', "150px");
			$("#photo" + i).addClass("pure-img-responsive");
		}
	}
}