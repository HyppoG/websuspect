<!-- include header -->
<jsp:include page="../header.jsp" />  

<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored ="false" %>

<!-- body content -->
<div id="indexMain">
	<div class="container-fluid">
		<c:if test="${not empty successMessage}">
		  <div class="col-xs-12 alert alert-success" role="alert">${successMessage} </div>
		</c:if>
		<c:if test="${not empty errorMessage}">
		  <div class="col-xs-12 alert alert-danger" role="alert">${errorMessage}</div>
		</c:if>
		<div class="container-fluid">
			<div class="row"><div class="col-xs-12"><hr></div></div>
			<div class="col-xs-12 col-md-5"><strong>System Information</strong></div>
			<div class="row"><div class="col-xs-12"><hr></div></div>
		</div>

		<pre>
			<c:out value="${systemInfo}" />
		</pre>
	</div>
</div>
<!-- End body content -->

<!-- include footer -->
<jsp:include page="../footer.jsp" />  