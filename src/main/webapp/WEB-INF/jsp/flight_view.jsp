<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored ="false" %>
<!-- include header -->
<jsp:include page="header.jsp" />
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<c:url value="/images?file=${fn:replace(flight.name,' ','_')}-${flight.id}.jpg" var="imageUrl"> </c:url>

<div id="indexMain" class="container-fluid">	

	<c:if test="${not empty successMessage}">
	  <div class="col-xs-12 alert alert-success" role="alert">${successMessage} </div>
	</c:if>
	<c:if test="${not empty errorMessage}">
	  <div class="col-xs-12 alert alert-danger" role="alert">${errorMessage}</div>
	</c:if>
  	<div class="container-fluid" style="margin-top:30px;">
  		<div class="col-xs-offset-1 col-sm-offset-2">
  			<img src="${imageUrl}" alt="${flight.name}" class="img-responsive" width="200" height="400"/>
  		</div>
  	</div>
  	<div class="container-fluid" style="margin-top:30px;">
  		<div class="col-xs-offset-1 col-sm-offset-2 col-sm-2">
  			<p class="text-center"><mark>${flight.name}</mark></p>
  		</div>
  	</div>
  	<div class="form-group" style="margin-top:30px;">
       <div class="col-xs-5 col-sm-2">
    	  <a href="javascript:history.back()" class="btn btn-primary btn-group-justified btn-sm">Back</a>
       </div>
    </div>
</div>
<!-- include footer -->    
<jsp:include page="footer.jsp" />