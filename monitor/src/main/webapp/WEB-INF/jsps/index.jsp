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
<link rel="stylesheet" href="resources/css/monitor.css">


<!--[if lt IE 9]>
    <script src="http://cdnjs.cloudflare.com/ajax/libs/html5shiv/3.7/html5shiv.js"></script>
<![endif]-->

<link rel="shortcut icon" href="resources/images/rebound-small.ico" />
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
					<li id="home"><a href="/">Home</a></li>
					<li id="control"><a href="status">Enable / Disable</a></li>

					<li id="schedule"><a href="#">Schedule</a>
					</li>

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

			<div id="homePage" class="content" >
				<h2 class="content-subhead">At a Glance</h2>
				<p>
					<c:choose>
						<c:when test="${monitorData.status eq 'ENABLED'}">
							<c:set var="textStyle" value="normalStatusText"></c:set>
						</c:when>
						<c:when test="${monitorData.status eq 'FAILED'}">
							<c:set var="textStyle" value="warningText"></c:set>
						</c:when>
						<c:when test="${monitorData.status eq 'DISABLED'}">
							<c:set var="textStyle" value="abnomrmalStatusText"></c:set>
						</c:when>
						<c:otherwise>
							<c:set var="textStyle" value="unknownStatusText"></c:set>
						</c:otherwise>
					</c:choose>
					<b>Monitor Status: </b><span class="${textStyle}">${monitorData.status}</span> <br>
					<b>Days Up: </b> ${monitorData.daysUp} <br>
					<b>Number of Detections (lifetime): </b> ${monitorData.numDetection} <br>
					<b>Most Recent Detection On: </b> ${monitorData.mostRecentDetectionDate }
				</p>

				<h2 class="content-subhead">Most recent photo captures <i>... a future capability.</i></h2>
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
			</div>
		</div>
	</div>
	


	<script src="resources/js/jquery-2.1.1.min.js"></script>
	
	<script src="resources/js/monitor.js"></script>

</body>
</html>
