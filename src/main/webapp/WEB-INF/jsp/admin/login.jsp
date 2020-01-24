<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored ="false"%>
<c:url var="registerURL" value="/registration"/>
<!-- include header -->
<jsp:include page="../header.jsp" />  

<!-- body container -->

<div class="login-form">

	<c:if test="${not empty successMessage}">
		<div id="success_message" class="col-xs-12 alert alert-success"
			role="alert">${successMessage}</div>
	</c:if>
	<c:if test="${not empty errorMessage}">
		<div id="error_message" class="col-xs-12 alert alert-danger"
			role="alert">${errorMessage}</div>
	</c:if>
	<c:if test="${not empty errors}">
		<div class="row" id="error_messages">
			<c:forEach var="error" items="${errors}">
				<div class="col-xs-12 alert alert-danger" role="alert">${error.value}</div>
			</c:forEach>
		</div>
	</c:if>

		<div id="login">
			<h1 class="header-login">Admin Login</h1>

			<form id="adminLoginForm" class="login-form" action="admin/authenticate"
				method="post">

				<div class="field-wrap">
					<div class="has-error">
							<div class="error" id="key_error">${key_error}</div>
					</div>
					<label class="label-login"> Superkey<span class="req">*</span>
					</label> <input type="text" class="login-input" name="key" id="key" maxlength=30>
		                

				</div>

				<button class="button login-button button-block" />
				Log In

			</form>

		</div>

	</div>

   
<!-- End body content -->
<!-- include footer -->
<jsp:include page="../footer.jsp" />