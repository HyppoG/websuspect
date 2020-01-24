<!-- include header -->
<jsp:include page="../header.jsp" />  

<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored ="false" %>

<!-- body content -->

<div id="indexMain" class="container-fluid" style="margin-bottom: 15px;">

	<div class="row">
		<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12 text-center">
			<h2>Upload new "shipping.xml" file</h2>
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
		<c:url var="uploadFile" value="/admin/settings/upload"></c:url>
		<form:form class="form-horizontal" id="uploadForm" 
							onsubmit="return validateForm()"  enctype="multipart/form-data" action="${uploadFile}" method="post">
			<input type="hidden" name="${csrfTokenParam}" value="${csrfToken}" />
			<div class="row">
				<div class="col-lg-6">
				  <div class="form-group">
					    <label for="inputFile" class="form-label">Select File</label>
					      <input type="file" class="form-control" id="fileName" name="fileName" placeholder="Select file to upload" 
					     				onkeyup="checkForEmpty('fileName','fileName_error')">
					      	<div class="has-error">
								<div class="error" id="fileName_error">${fileNameError}</div>
							</div>
				  </div>
				</div>
			</div>   
						<button type="submit" class="btn btn-primary btn-group-justified btn-sm">Upload</button>
		</form:form>
	</div>
	</div>
</div>
<!-- End body content -->

<script>
function validateForm() {
    var uploadForm = document.forms.uploadForm;
    var isFormEmpty=false;
    if(!checkForEmpty('fileName','fileName_error')){
   	   isFormEmpty=true;
	}
	if(isFormEmpty)return false;
       return true;
}
</script>
<!-- include footer -->
<jsp:include page="../footer.jsp" />  
