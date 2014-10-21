<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<html>
  <head>
  	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<meta name="description"
		content="A layout example with a side menu that hides on mobile, just like the Pure website.">
	
	
	<link rel="stylesheet"
		href="http://yui.yahooapis.com/pure/0.5.0/pure-min.css">
	
	
	<link rel="stylesheet" href="resources/css/side-menu.css">
	<link rel="stylesheet" href="resources/css/monitor.css">
	
	<!--[if lt IE 9]>
	    <script src="http://cdnjs.cloudflare.com/ajax/libs/html5shiv/3.7/html5shiv.js"></script>
	<![endif]-->
	<link rel="shortcut icon" href="resources/images/rebound-small.ico" >
    <title><tiles:getAsString name="title"/></title>
  </head>
  <body>
        
          <tiles:insertAttribute name="header" />
        
          <tiles:insertAttribute name="menu" />
        
          <tiles:insertAttribute name="body" />
        
          <tiles:insertAttribute name="footer" />
        
  </body>
</html>