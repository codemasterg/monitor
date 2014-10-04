<%@ page import="java.util.Date"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<link rel="stylesheet" href="resources/css/monitor.css">
	<hr style="margin-top: 5%"/>
	<footer>
		<div>
			<c:set var="now" value="<%=new java.util.Date()%>" />
			<label class="footerDate">Page Updated On <fmt:formatDate type="both" value="${now}" /></label>
			<label class="footerLabel">&copy; Greg Totsline 2014</label>
		</div>
	</footer>
