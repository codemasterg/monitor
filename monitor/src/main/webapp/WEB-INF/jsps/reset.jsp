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
					<li id="control"><a href="status">Enable / Disable</a></li>
					<li id="reset" class="menu-item-divided pure-menu-selected"><a href="reset">Reset</a></li>
					<li id="log"><a href="log">Log</a></li>
				</ul>
			</div>
		</div>

		<div id="main">
			<%@ include file="header.jsp" %>

			<div class="content">
				<p id="resetText">Do you want to reset all statistics and remove all photos?</p>
				<input type='button' value="Reset" id='resetButton'>
			</div>
		</div>
		<%@ include file="footer.jsp" %>
	</div>

	<script src="resources/js/jquery-2.1.1.min.js"></script>
	<script src="resources/js/monitor.js"></script>

</body>
</html>
