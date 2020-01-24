<!-- include header -->
<jsp:include page="header.jsp" />
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored ="false" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:url var="logintUrl" value="/login"/>
  	<div id="indexMain" class="container-fluid">
  		<div class="col-sm-10 col-md-11">
  			<p class="bg-danger">Application Error. Please contact to administrator.Go to <a href="${logintUrl}">Login page</a></p>
  		</div>
  	</div>
</body>
</html>