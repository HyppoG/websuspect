<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"
	isELIgnored="false"%>
<c:url var="registerURL" value="/registration" />
<c:url var="oldLoginURL" value="/oldlogin" />
<c:url var="addUser" value="/registerUser"></c:url>
<!-- include header -->
<jsp:include page="header.jsp" />

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

	<ul class="tab-group">
		<c:if test="${register==false}">
			<li class="tab active"><a class=".login-a" href="#login">Log In</a></li>
			<li class="tab"><a class=".login-a" href="#signup">Sign Up</a></li>
		</c:if>
		<c:if test="${register==true}">
			<li class="tab"><a class=".login-a" href="#login">Log In</a></li>
			<li class="tab active"><a class=".login-a" href="#signup">Sign Up</a></li>
		</c:if>
	</ul>

	<div class="tab-content">
		<c:if test="${register==false}">
			<div id="login" class="tab-pane show active" role="tabpanel">
		</c:if>
		<c:if test="${register==true}">
			<div id="login" class="tab-pane" role="tabpanel">
		</c:if>
			<h1 class="header-login">Welcome Back!</h1>

			<form id="adminLoginForm" class="login-form" action="authenticate"
				method="post">

				<div class="field-wrap">
					<div class="has-error">
						<div class="error" id="username_error">${username_error}</div>
					</div>
					<label class="label-login"> Username<span class="req">*</span>
					</label> <input class="login-input" type="text" name="username" value="${param.username}"
						id="username" maxlength="30">

				</div>

				<div class="field-wrap">
					<div class="has-error">
						<div class="error" id="passwd_error">${password_error}</div>
					</div>
					<label class="label-login"> Password<span class="req">*</span>
					</label> <input class="login-input"  type="password" name="password" id="password">

				</div>

				<p class="forgot">
					<a href="passwordRecovery">Forgot Password ?</a>
				</p>
				<p class="forgot">
					You are an old user ? Please login via this <a href="${oldLoginURL}">form</a>
				</p>

				<button class="button login-button button-block">Log In</button>

			</form>

		</div>
		<c:if test="${register==false}">
			<div id="signup" class="tab-pane" role="tabpanel">
		</c:if>
		<c:if test="${register==true}">
			<div id="signup" class="tab-pane show active" role="tabpanel">
		</c:if>
			<h1 class="header-login">Sign Up for Free</h1>

			<form:form method="POST" modelAttribute="user" action="${addUser}"
				class="login-form" id="userForm">

				<input type="hidden" name="${csrfTokenParam}" value="${csrfToken}" />
				<div class="field-wrap">
					<c:set var="usernameError"><form:errors path="username" class="invalid-feedback d-block" /></c:set>
						${usernameError}
					<label for="username" class="label-login"> Username<sup>*</sup></label>
					<form:input type="text" class="login-input" path="username"
						id="username" maxLength="30"
						onkeyup="checkForEmpty('username','username_error','Username')" />
				</div>

				<div class="field-wrap">
					<c:set var="emailError"><form:errors path="email" class="invalid-feedback d-block" /></c:set>
						${emailError}
					<label for="email" class="label-login"> Email<sup>*</sup></label>
					<form:input type="text" class="login-input" path="email"
						id="email" maxLength="70"
						onkeyup="checkForEmpty('email','email_error','Email')" />
				</div>

				<div class="field-wrap">
					<c:set var="birthdayError"><form:errors path="birthday" class="invalid-feedback d-block" /></c:set>
						${birthdayError}
					<label for="birthday" class="label-login">Birthday</label>
					<form:input data-provide="datepicker" data-date-format="dd/mm/yyyy"
						class="login-input datepicker" path="birthday" name="birthday"
						/>
					
				</div>
				
				<div class="field-wrap">
					<c:set var="passwordError"><form:errors path="password" class="invalid-feedback d-block" /></c:set>
						${passwordError}
					<label for="password" class="label-login"> Password <sup>*</sup></label>
					<form:input type="password" class="login-input" path="password"
						id="password" 
						onkeyup="checkForEmpty('password','password_error','Password')" />
				</div>
				
				<div class="field-wrap">
					<c:set var="password2Error"><form:errors path="password2" class="invalid-feedback d-block" /></c:set>
						${password2Error}
					<label for="password2" class="label-login">Retype
						Password<sup>*</sup>
					</label>
					<form:input type="password" class="login-input" path="password2"
						id="password2" 
						onkeyup="checkForEmpty('password2','password2_error','Retype Password')"
						maxLength="30"></form:input>
				</div>

				<button type="submit" class="button login-button button-block">Get Started</button>
				

			</form:form>

		</div>

	</div>

	<!-- <ul class="bg-bubbles">
					<li></li>
					<li></li>
					<li></li>
					<li></li>
					<li></li>
					<li></li>
					<li></li>
					<li></li>
					<li></li>
					<li></li>
				</ul>-->

</div>
<!-- End body content -->
<!-- include footer -->
<jsp:include page="footer.jsp" />