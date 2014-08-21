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

<title>Monitor Manager &ndash; Based on Yahoo Pure Layout Examples &ndash; Pure</title>

<link rel="stylesheet"
	href="http://yui.yahooapis.com/pure/0.5.0/pure-min.css">


<link rel="stylesheet" href="resources/css/side-menu.css">


<!--[if lt IE 9]>
    <script src="http://cdnjs.cloudflare.com/ajax/libs/html5shiv/3.7/html5shiv.js"></script>
<![endif]-->


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
					<li id="control"><a href="#">Stop/Start</a></li>

					<li id="schedule"><a href="#">Schedule</a>
					</li>

					<li id="statistics"><a href="#">Statistics</a></li>
					<li id="log"><a href="#">Log</a></li>
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
				<h2 class="content-subhead">At a Glance</h2>
				<p>
					<b>Days Up:</b> ${monitorData.daysUp} <br>
					<b>Number of Intrusions in the Past Month:</b> ${monitorData.numDetection} <br>
					<b>Most Recent Detection On:</b> ${monitorData.mostRecentDetectionDate }
				</p>

				<h2 class="content-subhead">Most recent photo captures</h2>
				<p>.</p>

				<div class="pure-g">
					<div class="pure-u-1-4">
						<img class="pure-img-responsive"
							src="https://lh4.googleusercontent.com/DCTDK2hXslKlEPfXgE4Aqs8NL_qia5OVUWomqhzYBA0=w305-h228-p-no"
							alt="Peyto Lake">
					</div>
					<div class="pure-u-1-4">
						<img class="pure-img-responsive"
							src="https://lh3.googleusercontent.com/Rqvb6xY-GIVtkv9vlMkcCWqaldLjbdDYmN-YVqnyDbc=w305-h228-p-no"
							alt="Train">
					</div>
					<div class="pure-u-1-4">
						<img class="pure-img-responsive"
							src="https://lh5.googleusercontent.com/cJYrigMahU5LwBsCkTIJTvgOD4jE-s7yrySnjC6kC1g=w276-h207-p-no"
							alt="T-Shirt Store">
					</div>
					<div class="pure-u-1-4">
						<img class="pure-img-responsive"
							src="https://lh5.googleusercontent.com/NhIGuE5VzXnkqvach_F93Zgytk_6jBUTpeIvUkys4-0=w306-h228-p-no"
							alt="Mountain">
					</div>
				</div>


				<p id="controlText" hidden="true">Click to enable / disable
					email notifications and photo capture.
					
					<input type='button' value='Disable' id='controlButton'>
				</p>
			</div>
		</div>
	</div>
	


	<script src="resources/js/jquery-2.1.1.min.js"></script>
	
	<script src="resources/js/monitor.js"></script>

</body>
</html>
