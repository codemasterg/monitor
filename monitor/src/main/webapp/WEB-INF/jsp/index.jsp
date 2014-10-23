<%@ page import="java.util.Date"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>


	<div id="layout">


		<div id="main_content">

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
								<img class="pure-img-responsive" id="photo${status.count}" title="click to enlarge, click again to reduce"
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
		
	</div>
	
	<script src="resources/js/jquery-2.1.1.min.js"></script>
	
	<script src="resources/js/monitor.js"></script>

