<!-- include header -->
<jsp:include page="header.jsp" />

<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:url value="/passwordRecovery" var="forgotPasswordURL" />
<c:url value="/login" var="loginURL" />

<!-- body content -->
<div id="indexMain" class="container-fluid" style="margin-bottom: 15px;">

	<div class="row">
		<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12 text-center">
			<h2>Password recovery</h2>
		</div>
	</div>

	<c:if test="${not empty successMessage}">
		<div class="col-xs-12 alert alert-success" role="alert">${successMessage}
		</div>
	</c:if>
	<c:if test="${not empty errorMessage}">
		<div class="col-xs-12 alert alert-danger" role="alert">${errorMessage}</div>
	</c:if>

	<div class="row">
		<div class="col-xs-6 col-sm-6 col-md-6 col-lg-6 offset-sm-3">

			<form:form class="form-horizontal" id="passwordRecoveryForm"
				enctype="multipart/form-data" action="${forgotPasswordURL}"
				method="post">

				<input type="hidden" name="${csrfTokenParam}" value="${csrfToken}" />
					<div class="form-group">
							<label>Your email address:</label> 
							<input class="form-control"
								name="email" value="${fn:escapeXml(param.email)}" />
						</div>
					<div class="form-group">
						<a href="${loginURL}" onclick="history.back()"
							class="btn btn-primary  action-button previous_button">Back</a>
						<button  class="btn btn-primary  action-button next" type="submit">Recover</button>
					</div>
			</form:form>
		</div>
	</div>
</div>

<jsp:include page="footer.jsp" />