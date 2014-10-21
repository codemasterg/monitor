<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml" %>

	<div id="layout">


		<div id="main">

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