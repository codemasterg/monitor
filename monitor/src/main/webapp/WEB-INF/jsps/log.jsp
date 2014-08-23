<%@ page import="java.util.Date"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml" %>



<!doctype html>

<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description"
	content="A layout example with a side menu that hides on mobile, just like the Pure website.">

<title>Monitor Manager &ndash; Layout on Yahoo Pure &ndash; Pure</title>

<link rel="stylesheet"
	href="http://yui.yahooapis.com/pure/0.5.0/pure-min.css">


<link rel="stylesheet" href="resources/css/side-menu.css">
<link rel="stylesheet" href="resources/css/monitor.css">

<!--[if lt IE 9]>
    <script src="http://cdnjs.cloudflare.com/ajax/libs/html5shiv/3.7/html5shiv.js"></script>
<![endif]-->
<link rel="shortcut icon" href="resources/images/rebound-small.ico" >

</head>
<body>


	<div id="layout">
		<!-- Menu toggle -->
		<a href="#menu" id="menuLink" class="menu-link"> <!-- Hamburger icon -->
			<span></span>
		</a>

		<div id="menu">
			<div class="pure-menu pure-menu-open">
				<a class="pure-menu-heading" href="#">Functions</a>

				<ul>
					<li id="home"><a href="#">Home</a></li>
					<li id="control"><a href="/">Enable / Disable</a></li>

					<li id="schedule"><a href="#">Schedule</a>
					</li>

					<li id="statistics"><a href="#">Statistics</a></li>
					<li id="log"><a href="log">Log</a></li>
				</ul>
			</div>
		</div>

		<div id="main">
			<div class="header">
				<c:if test="">
				</c:if>
				<div>
					<span class="header" style="font-size: 25pt">Watch Dog</span>
					<img style="height: 70px; width: 60px; vertical-align: middle" alt="Downdee" src="resources/images/rebound.jpg">
				</div>
					
				
				<h2>Motion Detection Manager</h2>
			</div>

						<div class="content">
				<!-- hidden elements at startup, made visible based on user menu selection.  See monitor.js -->
				
				<p id="controlText" hidden="true">Click to enable / disable
					email notifications and photo capture.
					
					<input type='button' value='Disable' id='controlButton'>
				</p>
				
				<p id="logText" >Log viewer, most recent appear at bottom. <br>
					
					<textarea id="logTextArea" class="logText"><c:forEach var="rec" items="${logRecord}" varStatus="loopCounter">${rec}</c:forEach></textarea>
				</p>
			</div>
		</div>
	</div>

	<!-- make textarea above scroll bar auto scroll to bottom since newest recs at bottom -->
	<script type="text/javascript">
		var textarea = document.getElementById('logTextArea');
		textarea.scrollTop = textarea.scrollHeight;
	</script>

	<script src="resources/js/jquery-2.1.1.min.js"></script>
	
	<script src="resources/js/monitor.js"></script>

</body>
</html>
