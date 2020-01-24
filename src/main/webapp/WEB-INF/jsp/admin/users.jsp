<!-- include header -->
<jsp:include page="../header.jsp" />

<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"
	isELIgnored="false"%>

<c:url var="addUserUrl" value="/admin/addUser" />
<c:url var="addAdminUrl" value="/admin/addAdmin" />
<c:url var="editUserUrl" value="/admin/editUser" />
<c:url var="deleteUserUrl" value="/admin/deleteUser" />

<!-- body content -->
<div id="indexMain" class="container center">
	<c:if test="${not empty successMessage}">
		<div class="col-xs-12 alert alert-success" role="alert">${successMessage}
		</div>
	</c:if>
	<c:if test="${not empty errorMessage}">
		<div class="col-xs-12 alert alert-danger" role="alert">${errorMessage}</div>
	</c:if>


		<div class="card card-cascade narrower">

			<!--Card image-->
			<div class="view view-cascade gradient-card-header blue-gradient narrower py-2 mx-4 mb-3 d-flex justify-content-between align-items-center">
				<div></div>

				<a href="" class="white-text mx-3">Users table</a>
				<div>
					<a href="${addUserUrl}" id="addUrl"
						class="btn btn-outline-white btn-rounded btn-sm px-2"><i
						class="fas fa-plus-circle mt-0"> Add new user</i></a>
					<a href="${addAdminUrl}" id="addUrl"
						class="btn btn-outline-white btn-rounded btn-sm px-2"><i
						class="fas fa-plus-circle mt-0"> Add new admin</i></a>
				</div>
			</div>
			<!--/Card image-->

			<div class="px-4">
				<div class="table-wrapper">
					<!--Table-->
					<table class="table table-hover mb-0">
						<!--Table head-->
						<thead>
							<tr>
								<th class="th-lg"><a>Id <i class="fas fa-sort ml-1"></i></a></th>
								<th class="th-lg"><a href="">Name <i class="fas fa-sort ml-1"></i></a></th>
								<th class="th-lg"><a href="">Email <i class="fas fa-sort ml-1"></i></a></th>
								<th class="th-lg"><a href="">Role <i class="fas fa-sort ml-1"></i></a></th>
								<th class="th-lg"><a href="">Action <i class="fas fa-sort ml-1"></i></a></th>
							</tr>
						</thead>
						<!--Table head-->

						<!--Table body-->
						<tbody>

							<c:forEach items="${userList}" var="user">
								<tr>
									<td><b>${user.id}</b></td>
									<td><b>${user.username}</b></td>
									<td><b>${user.email}</b></td>
									<c:if test="${user.role_id == 1001}">
										<td><b>Admin</b></td>
									</c:if>
									<c:if test="${user.role_id == 1002}">
										<td><b>User</b></td>
									</c:if>
									<td align="center"><a href="${deleteUserUrl}/${user.id}"
										class="btn btn-primary btn-xs">Remove</a> <a
										href="${editUserUrl}/${user.id}"
										class="btn btn-primary btn-xs">Edit</a></td>
								</tr>
							</c:forEach>

						</tbody>
						<!--Table body-->
					</table>
					<!--Table-->
				</div>
			</div>
	</div>
</div>

<!-- End body content -->
<script type="text/javascript">
	function submitFormToEditUser() {
		if (validateUsername()) {
			document.getElementById("homeForm").action = "${editUserUrl}";
			document.getElementById("homeForm").submit();
		} else {
			showUsernameError();
		}
	}
	function submitFormToDelete() {
		if (validateUsername()) {
			var user_el = document.getElementById('username');
			var user = user_el.options[user_el.selectedIndex].innerHTML;
			var decision = confirm("Do you really want to delete user " + user
					+ ' ?');
			if (decision == false)
				return;
			document.getElementById("homeForm").action = "${deleteUserUrl}";
			document.getElementById("homeForm").submit();
		} else {
			showUsernameError();
		}
	}
	function validateUsername() {
		if (document.getElementById("username").value == -1)
			return false;
		return true;
	}
	function showUsernameError() {
		if (document.getElementById("username").value == -1) {
			document.getElementById("username_error").innerHTML = "Please select the username.";
		} else {
			document.getElementById("username_error").innerHTML = "";
		}
	}
</script>
<!-- include footer -->
<jsp:include page="../footer.jsp" />