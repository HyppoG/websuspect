<!-- include header -->
<jsp:include page="header.jsp" />

<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"
	isELIgnored="false"%>
<c:url var="loginURL" value="/login" />
<c:url var="updateProfile" value="/profile"></c:url>
<!-- body content -->

<div id="indexMain" class="container">
	<div class="row">
		<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12 text-center">
			<h2>Profile</h2>
		</div>
	</div>
	
	<c:if test="${not empty successMessage}">
		<div class="col-xs-12 alert alert-success" role="alert">${successMessage}
			Click to <a href="${loginURL}">Login.</a>
		</div>
	</c:if>
	<c:if test="${not empty errorMessage}">
		<div class="col-xs-12 alert alert-danger" role="alert">${errorMessage}</div>
	</c:if>

	<div class="row">
		<div class="col-xs-6 col-sm-6 col-md-6 col-lg-6 offset-sm-3">
			<form:form method="POST" modelAttribute="user"
				action="${updateProfile}" class="form-horizontal" id="userForm">
				<input type="hidden" name="${csrfTokenParam}" value="${csrfToken}" />
				<form:hidden path="id" />
				<div class="form-group">
					<c:set var="usernameError">
						<form:errors path="username" class="invalid-feedback d-block" />
					</c:set>
					<label for="username" class="form-label"> Username<sup>*</sup></label>
					<form:input type="text" class="form-control" path="username"
						id="username" placeholder="Username" maxLength="30"
						onkeyup="checkForEmpty('username','username_error','Username')"/>
					${usernameError}
				</div>

				<div class="form-group">
					<c:set var="emailError">
						<form:errors path="email" class="invalid-feedback d-block" />
					</c:set>
					<label for="email" class="form-label"> Email<sup>*</sup></label>
					<form:input type="text" class="form-control" path="email"
						id="email" placeholder="Email" maxLength="70"
						onkeyup="checkForEmpty('email','email_error','Email')"/>
						${emailError}
				</div>

				<div class="form-group">
					<c:set var="passwordError">
						<form:errors path="password" class="invalid-feedback d-block" />
					</c:set>
					<label for="password" class="form-label"> Password <sup>*</sup></label>
					<form:input type="password" class="form-control" path="password"
						id="password" placeholder="Password"
						onkeyup="checkForEmpty('password','password_error','Password')" />
						${passwordError}
				</div>

				<div class="form-group">
					<c:set var="password2Error">
						<form:errors path="password2" class="invalid-feedback d-block" />
					</c:set>
					<label for="password2" class="form-label">Retype Password<sup>*</sup></label>
					<form:input type="password" class="form-control" path="password2"
						id="password2" placeholder="Retype Password"
						onkeyup="checkForEmpty('password2','password2_error','Retype Password')"
						maxLength="30"></form:input>
						${password2Error}
				</div>

				<div class="form-group">
					<c:set var="birthdayError">
						<form:errors path="birthday" class="invalid-feedback d-block" />
					</c:set>
					<label for="birthday" class="form-label">Birthday</label>
					<form:input data-provide="datepicker" data-date-format="dd/mm/yyyy"
						class="form-control datepicker" path="birthday" name="birthday" />
						${birthdayError}
				</div>

				<div class="text-center">
					<form:button type="submit"
						class="btn btn-sm btn-primary btn-group-justified">Submit</form:button>
				</div>
			</form:form>
		</div>
	</div>
</div>
<!-- End body content -->

<!-- include footer -->
<jsp:include page="footer.jsp" />