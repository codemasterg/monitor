<%@ page import="java.util.Date"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>


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
					<li id="home" class="menu-item-divided pure-menu-selected"><a href="/">Home</a></li>
					<li id="control"><a href="status">Enable / Disable</a></li>
					<li id="reset"><a href="reset">Reset</a></li>
					<li id="log"><a href="log">Log</a></li>
				</ul>
			</div>
		</div>

		<div id="main">
			<div class="header">
				<c:if test="">
				</c:if>
				<div>
					<span class="header" style="font-size: 25pt">Watch Dog</span> <img
						style="height: 70px; width: 60px; vertical-align: middle"
						alt="Downdee" src="resources/images/rebound.jpg">
				</div>


				<h2>Motion Detection Manager</h2>
			</div>

			<br>
			<div id="homePage" class="content">
				<div class="pure-g">
					<div class="pure-u-1-2">
							<label class="headerLabel">At a Glance</label>
					</div>
					<div class="pure-u-1-2">
						<img alt="silence alarm" src="resources/images/mute-2-icon.png" title="Alarm will re-arm after configurable period." 
							id="silenceButton" style="height: 50px; width: 50px" >
						<label>Silence Alarm</label>
					</div>
				</div>
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
					<b>Monitor Status: </b><span class="${textStyle}">${monitorData.status}</span>
					<br> 
					<b>Days Up: </b> ${monitorData.daysUp} <br>
					 <b>Number of Detections (since reset): </b> ${monitorData.numDetection} <br> 
					 <b>Most Recent Detection On: </b> ${monitorData.formattedMostRecentDetectionDate }
				</p>

				<h2 class="content-subhead">Most recent photo captures</h2>
				<p>.</p>

				<!-- show most recent four photos captured.  Note that Jetty allows access to resources outside WAR in <JETTY HOME>/webapps/files -->
				<div class="pure-g">
					<c:set var="listLength" value="${fn:length(monitorData.photoList)}" />
					<c:forEach items="${monitorData.photoList}" varStatus="status">
						<c:if test="${status.index < 4}">
							<div class="pure-u-1-4">
								<img class="pure-img-responsive"
									src="/files/${monitorData.photoList[listLength - status.count].name}"
									alt="No photo available"/>
									<div>
										${monitorData.photoList[listLength - status.count].name}
									</div>
							</div>
						</c:if>
					</c:forEach>
				</div>
			</div>
		</div>



		<script src="resources/js/jquery-2.1.1.min.js"></script>
	
	<script src="resources/js/monitor.js"></script>
	<script src="resources/js/ui.js"></script>

</body>
</html>
