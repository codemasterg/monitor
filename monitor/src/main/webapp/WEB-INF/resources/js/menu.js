/**
 * Implement the behavior of index, log, and control JSPs.
 */

/**
 * Register click handlers following menu page load
 */
$(document).ready(function(){
	
  // user requested enable/disable of email notifications and photo taking
  $("#control").click(function(event){
	event.preventDefault();
    
    requestMonitorStatus();
  });
  
  // highlight menu choice based on page being loaded so user gets feedback on where in the 
  // navigation they are.
  highlightMenuItem();
  
  // user requested log viewer
  $("#log").click(function(event){
	loadLog();
	event.preventDefault();    
  });
  
  
  // home section is also default on 1st page load
  $("#home").click(function(event){
		event.preventDefault();
	    $("#homePage").show();
	 
	    window.location.pathname = "/monitor";
	  });
   
});

function loadLog()
{
	window.location.pathname = "/monitor/log";	
}

/**
 * Auto highlight a menu item based on the page loaded (i.e. the page in view)
 */
function highlightMenuItem()
{
	  //deselectAll();
	  if ($("#main_content").length)
	  {
		  $("#home").addClass("menu-item-divided pure-menu-selected");
	  }
	  else if ($("#control_content").length)
	  {
		  $("#control").addClass("menu-item-divided pure-menu-selected");
	  }
	  else if ($("#log_content").length)
	  {
		  $("#log").addClass("menu-item-divided pure-menu-selected");
	  }	
	  else if ($("#reset_content").length)
	  {
		  $("#reset").addClass("menu-item-divided pure-menu-selected");
	  }	
}

/**
 * Redirect to fetch status page that includes control button
 */
function requestMonitorStatus()
{
	window.location.pathname = "/monitor/status";
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