<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored ="false"%>
<!-- include header -->
<jsp:include page="header_init.jsp" />  

<c:url var="homeURL" value="/"/>
<c:url var="logoutUrl" value="/logout?redirect=login"/>
<c:url var="loginURL" value="/login"/>
<c:url var="aboutUsURL" value="/about_us"/>
<c:url var="feedbackReportURL" value="/admin/feedbacks"/>
<c:url var="bookingURL" value="/order"/>
<c:url var="adminBookingURL" value="/admin/orders"/>
<c:url var="feedbackURL" value="/feedback"/>
<c:url var="profileURL" value="/profile"/>
<c:url var="cartURL" value="/cart"/>
<c:url var="settingURL" value="/admin/settings"/>
<c:url var="registerURL" value="/registration"/>
<c:url var="adminLoginURL" value="/admin"/>
<c:url var="usersURL" value="/admin/users"/>
<c:url var="dbaccessURL" value="/admin/dbaccess"/>
  <body>
    <nav class="app-toolbar navbar fixed-top navbar-expand-lg navbar-dark info-color scrolling-navbar">
        <a class="navbar-brand" href="${homeURL}"><strong>Websuspect</strong></a>
        <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarSupportedContent">
            <ul class="navbar-nav mr-auto">
  				<c:if test="${isValidUser==true}">
	  				<li class="nav-item">
			  			<a href="${homeURL}" class="nav-link">Home</a>
			  		</li>
	  			</c:if>
		  		<c:if test="${isAdmin==true && isValidUser==true}">
		  			<li class="nav-item">
			  			<a href="${adminBookingURL}" class="nav-link">Orders</a>
			  		</li>
			  		<li class="nav-item">
			  			<a href="${feedbackReportURL}" class="nav-link">Feedbacks</a>
			  		</li>
			  		<li class="nav-item">
			  			<a href="${settingURL}" class="nav-link">Settings</a>
			  		</li>
			  		<li class="nav-item">
			  			<a href="${usersURL}" class="nav-link">Users</a>
			  		</li>
			  		<li class="nav-item">
			  			<a href="${dbaccessURL}" class="nav-link">DB Access</a>
			  		</li>
			  	</c:if>
			  	<c:if test="${isValidUser==true && isAdmin==false}">
			  		<li class="nav-item">
			  			<a href="${bookingURL}" class="nav-link">Flight Booking</a>
			  		</li>
			  		<li class="nav-item">	
			  			<a href="${feedbackURL}" class="nav-link">Feedback</a>
			  		</li>
			  		<li class="nav-item">	
			  			<a href="${adminLoginURL}" class="nav-link">Admin</a>
			  		</li>
			  	</c:if>
			  	<li class="nav-item">
		  			<a href="${aboutUsURL}" class="nav-link">About</a>
		  		</li>
			</ul>
			<ul class="navbar-nav nav-flex">
                <c:choose>
			  		<c:when test="${isValidUser==true}">
		                <li class="nav-item dropdown ">
		                    <a class="nav-link dropdown-toggle" href="#" id="navbarDropdownMenu" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">
		                        Hello, ${username}
		                    </a>
		                    <div class="dropdown-menu dropdown-menu-right" aria-labelledby="navbarDropdownMenu">
		                        <a class="dropdown-item" href="${profileURL}">Profile</a>
		                        <a class="dropdown-item" href="${logoutUrl}">Log out</a>
		                    </div>
		                </li>
		                <li class="nav-item active">
		                    <a href="${cartURL}"><button type="button" class="btn btn-success btn-rounded">Cart</button></a>
		                </li>
                	</c:when>
			  		<c:otherwise>
		                <li class="nav-item active">
		                    <a href="${loginURL}"><button type="button" class="btn btn-warning btn-rounded">Log in</button></a>
		                </li>
                	</c:otherwise>
			  	</c:choose>
            </ul> 
  		</div>
  	</nav>
 