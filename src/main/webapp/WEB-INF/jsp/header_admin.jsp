<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored ="false"%>
<!-- include header -->
<jsp:include page="header_init.jsp" />  

<!-- validate the login user -->
<%
pageContext.setAttribute("login", true, PageContext.PAGE_SCOPE);
%>
<%
	/* session = request.getSession(false);
	if(session==null || session.isNew()){
		response.sendRedirect(AppConstants.APP_NAME+"/logout");
	}else{
		boolean isValidUser=(Boolean)session.getAttribute("isValidUser");
		int roleId=(Integer)session.getAttribute("role");
		if(isValidUser==false || roleId != AppConstants.CUSTOMER_ROLE_ID || roleId==0){
			response.sendRedirect(AppConstants.APP_NAME+"/logout");
		}
	} */
%>
<%-- <c:if test="${empty isValidUser && empty role}">
	<c:url value="CloudFileUpload/logout" var="logout"/>
	<c:redirect url="${logout}"></c:redirect>
</c:if> --%>

<c:url var="logoutUrl" value="/logout"/>
<c:url var="homeURL" value="/home"/>
<c:url var="feedbackURL" value="/feedbackReport"/>
<c:url var="settingURL" value="/settings"/>
  <body>
  	<div class="container-fluid">
  		<div class="col-sm-2 col-md-4">
  			<h3>Ecommerce App</h3>
  		</div>
  		<div class="col-sm-10 col-md-8">
	  		<div class="col-sm-4 col-md-2" style="padding-top:10px;">
	  			<a href="${homeURL}" class="btn btn-primary btn-group-justified btn-sm">Home</a>
	  		</div>
	  		<div class="col-sm-4 col-md-3" style="padding-top:10px;">
	  			<a href="${homeURL}" class="btn btn-primary btn-group-justified btn-sm">About Us</a>
	  		</div>
	  		<div class="col-sm-4 col-md-3" style="padding-top:10px;">
	  			<a href="${homeURL}" class="btn btn-primary btn-group-justified btn-sm">Contact Us</a>
	  		</div>
	  		<div class="col-sm-4 col-md-2" style="padding-top:10px;">	
	  			<a href="${feedbackURL}" class="btn btn-primary btn-group-justified btn-sm">Feedbacks</a>
	  		</div>
	  		<div class="col-sm-4 col-md-2" style="padding-top:10px;">	
	  			<a href="${settingURL}" class="btn btn-primary btn-group-justified btn-sm">Settings</a>
	  		</div>
	  	<c:choose>	
	  		<c:when test="${login==true}">
		  		<div class="col-sm-4 col-md-2" style="padding-top:10px;">	
		  			<a href="${logoutUrl}" class="btn btn-primary btn-group-justified btn-sm">Logout</a>
		  		</div>
	  		</c:when>
	  		<c:otherwise>
	  		<div class="col-sm-4 col-md-2" style="padding-top:10px;">	
	  			<a href="${loginUrl}" class="btn btn-primary btn-group-justified btn-sm">Login</a>
	  		</div>
	  		</c:otherwise>
	  	</c:choose>
  		</div>
  	</div>
  	<div id="indexMain" class="container-fluid"><div class="col-md-4"><h4>Welcome ${username}</h4></div></div>