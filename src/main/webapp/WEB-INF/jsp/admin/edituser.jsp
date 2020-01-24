<!-- include header -->
<jsp:include page="../header.jsp" />

<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"
	isELIgnored="false"%>

<!-- body content -->
<div id="indexMain" class="container">
	<div class="row">
		<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12 text-center">
			<h2>
				<c:if test="${isEdit==true}">
		  		Edit User
		  	</c:if>
				<c:if test="${empty isEdit || isEdit==false}">
		  		Add New User
		  	</c:if>
			</h2>
		</div>
	</div>

	<div class="row">
		<c:if test="${not empty successMessage}">
			<div class="col-xs-12 alert alert-success" role="alert">${successMessage}
				Click to <a href="${loginURL}">Login.</a>
			</div>
		</c:if>
		<c:if test="${not empty errorMessage}">
			<div class="col-xs-12 alert alert-danger" role="alert">${errorMessage}</div>
		</c:if>

		<div class="col-xs-6 col-sm-6 col-md-6 col-lg-6 offset-sm-3">

			<c:url var="addUser" value="/admin/addUser"></c:url>
			<c:if test="${isEdit==true}">
				<c:url var="addUser" value="/admin/updateUser"></c:url>
			</c:if>
			<form:form method="POST" modelAttribute="user" action="${addUser}"
				class="form-horizontal" id="userForm">
				<input type="hidden" name="${csrfTokenParam}" value="${csrfToken}" />
				<form:hidden path="id" />
				<div class="form-group">
					<label for="user_name" class="form-label"> Username<sup>*</sup>&nbsp;&nbsp;&nbsp;
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					</label>
					<div class="col-xs-6 col-sm-5 col-md-3">
						<form:input type="text" class="form-control" path="user_name"
							id="user_name" placeholder="Username/Email" maxLength="30"
							onkeyup="checkForEmpty('username','username_error')" />
						<div class="has-error error" id="user_name_error">
							<form:errors path="username" class="error" />
						</div>
					</div>
				</div>

				<div class="form-group">
					<label for="role_id" class="form-label"> User Role<sup>*</sup>
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					</label>
					<div class="radioButton">
						<form:radiobuttons path="role_id" id="role_id" element="div"
							items="${userRolesList}" itemLabel="role_name" itemValue="id"
							onchange="checkRadioSelected('role_id','role_id_error')" />
						<div class="has-error">
							<form:errors path="role_id" id="role_id_error" class="error" />
						</div>
					</div>
				</div>

				<div class="form-group">
					<label for="password" class="form-label"> Password <sup>*</sup>
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					</label>
					<form:input type="password" class="form-control" path="password"
						id="password" placeholder="Password"
						onkeyup="checkForEmpty('password','password_error')" />
					<div class="has-error error" id="password_error">
						<form:errors path="password" cssClass="error" />
					</div>
				</div>


				<div class="form-group">
					<label for="password2" class="form-label">Retype Password<sup>*</sup></label>
					<form:input type="password" class="form-control" path="password2"
						id="password2" placeholder="Retype Password"
						onkeyup="checkForEmpty('password2','password2_error')"
						maxLength="30"></form:input>
					<div class="has-error error" id="password2_error">
						<form:errors path="password2" cssClass="error" />
					</div>
				</div>

				<form:button type="submit"
					class="btn btn-sm btn-primary btn-group-justified">Submit</form:button>
			</form:form>
		</div>
	</div>
</div>
<!-- End body content -->

<!-- include footer -->
<jsp:include page="../footer.jsp" />
