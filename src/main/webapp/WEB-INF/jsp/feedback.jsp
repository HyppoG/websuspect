<!-- include header -->
<jsp:include page="header.jsp" />

<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"
	isELIgnored="false"%>
<c:url var="feedbackUser" value="/feedback/add"></c:url>
<!-- body content -->
<div id="indexMain" class="container">

	<div class="row">
		<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12 text-center">
			<h4>We would love to hear back from you, did you like our
				product range or any other feedback? If so please fill in the form
				below.</h4>
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
			<form id="feedbackForm" modelAttribute="feedback" class="form-horizontal"
				method="post">
				<input type="hidden" name="${csrfTokenParam}" value="${csrfToken}" />
				<div class="form-group">
					<label for="username" class="form-label">Name <sup>*</sup>
					</label>
					<input type="text" class="form-control" path="username"
						id="username" placeholder="Name" maxLength="30"
						onkeyup="checkForEmpty('username','username_error','Username')" />
					<div class="has-error error" id="username_error">
						<errors path="username" class="error" />
					</div>
				</div>

				<div class="form-group">
					<label for="email" class="form-label">Email <sup>*</sup>
					</label>
					<input type="text" class="form-control" path="email"
						id="email" placeholder="Email" maxLength="70"
						onkeyup="checkForEmpty('email','email_error','Email')" />
					<div class="has-error error" id="email_error">
						<errors path="email" class="error" />
					</div>
				</div>

				<div class="form-group">
					<label for="feedback" class="form-label">Feedback <sup>*</sup>
					</label>
					<textarea cols="5" rows="4" class="form-control"
						path="feedback" id="feedbackField" placeholder="Feedback"
						onkeyup="checkForEmpty('feedbackField','feedbackField_error','Feedback')"></textarea>
					<div class="has-error error" id="feedbackField_error">
						<errors path="feedback" class="error" />
					</div>
				</div>

				<div class="form-group">
					<button id="buttonFeedback" type=submit
						class="btn btn-sm btn-primary btn-group-justified">Submit
						Feedback</button>
				</div>

			</form>
			
			<div style="display:none" id="dialog" title="Thank you !">Thank you for your feedback !</div>
			<div style="display:none" id="dialog2" title="Oh no !">An error occurred please try again</div>
		</div>
	</div>
</div>
<!-- End body content -->
<script type="text/javascript">
function validate() {
	var username = document.getElementById("username").value;
	var email = document.getElementById("email").value;
	var feedback = document.getElementById("feedbackField").value;
	var usernameError = document.getElementById('username_error');
	var emailError = document.getElementById('email_error');
	var feedbackError = document.getElementById('feedbackField_error');
	var isValid = true;
	if (username == "" || email == "" || feedback == "")
		isValid = false;
	if (username == "")
		usernameError.innerHTML = 'Name is required.';
	if (email == "")
		emailError.innerHTML = 'Email is required.';
	if (feedback == "")
		feedbackError.innerHTML = 'Feedback is required.';
	return isValid;
}
</script>
<!-- include footer -->
<jsp:include page="footer.jsp" />