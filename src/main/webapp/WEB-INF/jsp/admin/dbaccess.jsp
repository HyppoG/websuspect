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
			<h2>Access to database</h2>
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
			<form id="dbaccess">
				<div class="form-group">
					<label class="form-label">DB Login</label>
					<input type="text" class="form-control" name="db_login"/>
				</div>
				<div class="form-group">
					<label class="form-label">DB Password</label>
					<input type="text" class="form-control" name="db_password"/>
				</div>
			  	<input class="btn btn-sm btn-primary" type="submit" name="action" value="Connect to database">
			</form>
		</div>
	</div>
</div>

<!-- End body content -->

<!-- include footer -->
<jsp:include page="../footer.jsp" />
