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
  
  $("#controlButton").click(controlDetection);
  
  // select #home in menu by default
  $("#home").click();
});

function controlDetection()
{
	var buttonLabel = $(this).prop('value');
	
	// TODO make ajax call with action to perform
	
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