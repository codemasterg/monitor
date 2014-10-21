<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml" %>

	<div id="layout">


		<div id="main">

			<div class="content">
				<c:choose>
					<c:when test="${monitorData.status eq 'ENABLED'}">
						<c:set var="buttonLabel" value="Disable"></c:set>
						<c:set var="msg" value="Click to disable email notifications and photo capture."></c:set>
					</c:when>
					<c:when test="${monitorData.status eq 'DISABLED'}">
						<c:set var="buttonLabel" value="Enable"></c:set>
						<c:set var="msg" value="Email notifications and photo capture are currently disabled, click to enable."></c:set>
					</c:when>
					<c:otherwise>
						<c:set var="buttonLabel" value="Enable"></c:set>
						<c:set var="msg" value="The monitor had a failure or its state cannot be determined, click to enable."></c:set>
					</c:otherwise>
				</c:choose>
				<p id="controlText">${msg}</p>
				<input type='button' value="${buttonLabel}" id='controlButton'>
				
			</div>
		</div>
	</div>

	<script src="resources/js/jquery-2.1.1.min.js"></script>
	<script src="resources/js/monitor.js"></script>

