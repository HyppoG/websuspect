<!-- include header -->
<jsp:include page="../header.jsp" />

<%@ page contentType="text/html;charset=UTF-8" language="java"
	isELIgnored="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!-- body content -->


<div id="indexMain" class="container">

	<c:if test="${not empty successMessage}">
		<div class="col-xs-12 alert alert-success" role="alert">${successMessage}
		</div>
	</c:if>
	<c:if test="${not empty errorMessage}">
		<div class="col-xs-12 alert alert-danger" role="alert">${errorMessage}</div>
	</c:if>

	<div class="row">
		<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12 text-center">
			<h2>Feedback reports</h2>
		</div>
	</div>

	<div class="table-responsive">
		<table class="table">
			<thead>
				<tr>
					<th>Username</th>
					<th>Email</th>
					<th>Feedback</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${feedbacksList}" var="feedback">
					<tr>
						<td>${feedback.username}</td>
						<td>${feedback.email}</td>
						<td>${feedback.feedback}</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
</div>
<!-- End body content -->
<!-- include footer -->
<jsp:include page="../footer.jsp" />