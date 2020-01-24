<!-- include header -->
<jsp:include page="../header.jsp" />

<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"
	isELIgnored="false"%>
<c:if test="${isEdit==true}">
	<c:url var="updateUrl" value="/admin/updateUser"></c:url>
</c:if>
<c:if test="${(empty isEdit || isEdit==false) && isAdmin==false}">
	<c:url var="updateUrl" value="/admin/addUser"></c:url>
</c:if>
<c:if test="${(empty isEdit || isEdit==false) && isAdmin==true}">
	<c:url var="updateUrl" value="/admin/addAdmin"></c:url>
</c:if>
<!-- body content -->
<div id="indexMain" class="container">
	<div class="row">
		<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12 text-center">
			<h2>
				<c:if test="${isEdit==true}">
		  		Edit User
		  	</c:if>
				<c:if
					test="${(empty isEdit || isEdit==false) && isAdmin==false}">
		  		Add New User
		  	</c:if>
				<c:if
					test="${(empty isEdit || isEdit==false) && isAdmin==true}">
		  		Add New Admin
		  	</c:if>
			</h2>
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
			<form:form method="POST" modelAttribute="user" action="${updateUrl}"
				class="form-horizontal" id="userForm">
				<input type="hidden" name="${csrfTokenParam}" value="${csrfToken}" />
				<form:input type="hidden" path="role_id" id="role_id" />
				<form:input type="hidden" path="id" id="id" />
				<div class="form-group">
					<c:set var="usernameError">
						<form:errors path="username" class="invalid-feedback d-block" />
					</c:set>
					<label class="form-label" for="user_name">User Name</label>
					<form:input type="text" tabindex="1" class="form-control"
						path="username" id="username" placeholder="Username"
						maxLength="30" />
					${usernameError}
				</div>
				<div class="form-group">
					<c:set var="passwordError">
						<form:errors path="password" class="invalid-feedback d-block" />
					</c:set>
					<label class="form-label" for="password">Password</label>
					<form:input tabindex="3" type="password" class="form-control"
						path="password" id="password" placeholder="Password" />
					${passwordError}
				</div>
				<div class="form-group">
					<c:set var="password2Error">
						<form:errors path="password2" class="invalid-feedback d-block" />
					</c:set>
					<label class="form-label" for="password2">Retype password</label>
					<form:input tabindex="4" type="password" class="form-control"
						path="password2" id="password2" placeholder="Retype Password"></form:input>
					${password2Error}
				</div>
				<c:if test="${isAdmin=true}">
					<c:set var="superkeyError">
						<form:errors path="superkey" class="invalid-feedback d-block" />
					</c:set>
					<div class="form-group" id="superkeydiv">
						<label class="form-label" for="superkey">Superkey</label>
						<form:input class="form-control" path="superkey" id="superkey"
							placeholder="Superkey" maxLength="4"></form:input>
						${superkeyError}
					</div>
				</c:if>
				<div class="text-center">
					<c:if test="${isEdit==true}">
						<form:button type="submit" class="btn btn-start-order">Update</form:button>
					</c:if>
					<c:if test="${empty isEdit || isEdit==false}">
						<form:button type="submit" class="btn btn-start-order">Create</form:button>
					</c:if>
				</div>
			</form:form>
		</div>
	</div>
</div>

<!-- End body content -->

<!-- include footer -->
<jsp:include page="../footer.jsp" />
