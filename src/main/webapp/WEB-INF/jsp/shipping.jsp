<!-- include header -->
<jsp:include page="header.jsp" />
 
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored ="false" %>
<%@ page import="com.test.vulnerableapp.util.AppConstants" %>
<%
pageContext.setAttribute("normal", AppConstants.SHIPPING_NORMAL, PageContext.PAGE_SCOPE);
pageContext.setAttribute("express", AppConstants.SHIPPING_EXPRESS, PageContext.PAGE_SCOPE);
pageContext.setAttribute("super_express", AppConstants.SHIPPING_SUPER_EXPRESS, PageContext.PAGE_SCOPE);
%>
<c:url var="addShippingUrl" value="/addShipping"></c:url>
<!-- body content -->
    <div id="indexMain" class="container">
    
    	<br> <c:if test="${not empty successMessage}">
		  <div class="col-xs-12 alert alert-success" role="alert">${successMessage} </div>
		</c:if>
		<c:if test="${not empty errorMessage}">
		  <div class="col-xs-12 alert alert-danger" role="alert">${errorMessage}</div>
		</c:if>
		
		<div class="multi_step_form">  
			<form:form id="msform" modelAttribute="shippingDetails" action="${addShippingUrl}" method="post"> 
				<input type="hidden" name="${csrfTokenParam}" value="${csrfToken}" />
				<form:input type="hidden" path="orderId" id="orderId"/>
				<form:input type="hidden" path="type" id="type"/>
				<!-- Tittle -->
				<div class="tittle">
					<h2>Shipping</h2>
					<p>How quickly do you want it shipped ?</p>
				</div>
				<!-- progressbar -->
				<ul id="progressbar">
					<li class="active">Delivery</li>  
					<li class="active">Shipping</li> 
					<li>Payment</li>
					<li>Confirmation</li>
				</ul>
	    	
	    		<div class="offset-sm-3">
				<div class="form-group row">
		            <label for="normalSipping" class="col-form-label col-sm-5">
		            Normal Shipping (7 to 14 days) &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</label>
		            <div class="col-sm-2">
		                <input type="text" class="form-control text-right" id="normalShipping" 
		                			value="$ ${shippingPrice.get(normal)}" disabled/>
		            </div>
		             <div class="checkbox">
					    <input type="radio" id="" name="shippingName" value="${normal}">
					</div>
		        </div>
		        
		        <div class="form-group row">
		            <label for="expressSipping" class="col-form-label col-sm-5">
		            Express Shipping (7 to 10 days) &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</label>
		            <div class="col-sm-2">
		                <input type="text" class="form-control text-right" id="expressShipping" 
		                			value="$ ${shippingPrice.get(express)}" disabled/>
		            </div>
		            
		            <div class="checkbox">
					    <input type="radio" id="" name="shippingName" value="${express}">
					</div>
		        </div>
		        
		         <div class="form-group row">
		            <label for="superExpressSipping" class="col-form-label col-sm-5">
		            Super Express Shipping (2 to 3 days) &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</label>
		            <div class="col-sm-2">
		                <input type="text" class="form-control text-right" id="superExpressShipping" 
		                				value="$ ${shippingPrice.get(super_express)}" disabled/>
		            </div>
		             <div class="checkbox">
					    <input type="radio" id="" name="shippingName" value="${super_express}">
					</div>
		        </div>
		        </div>
				
				<button onclick="history.back()" class="action-button previous_button">Previous</button>
		        <!-- <button class="next action-button" onclick="validate();">Next</button> -->
		        <button class="next action-button" onclick="return validation();">Next</button>
		     </form:form>
	    </div>
	</div>
<!-- End body content -->
<script>
	function validate(){
		var radios = document.getElementsByName('shippingName');
		var type = document.getElementById('type');
		var shippingName;
		var isShippingSelected=false;

		for (var i = 0, length = radios.length; i < length; i++) {
		    if (radios[i].checked) {
		        shippingName = radios[i].value;
		        isShippingSelected = true;
		    }
		}
		if(isShippingSelected){
			type.value = shippingName;
		    document.getElementById("shippingForm").submit();
		    console.log("test");
		}else{
			alert("Please select any one Shipping.");

		}
	}
	
	function validation(){
		var radios = document.getElementsByName('shippingName');
		var type = document.getElementById('type');
		var shippingName;
		var isShippingSelected=false;

		for (var i = 0, length = radios.length; i < length; i++) {
		    if (radios[i].checked) {
		        shippingName = radios[i].value;
		        isShippingSelected = true;
		    }
		}
		if(isShippingSelected){
			type.value = shippingName;
		    //document.getElementById("shippingForm").submit();
		    return true;
		}else{
			alert("Please select any one Shipping.");
			return false;
		}
	}
</script>
<!-- include footer -->
<jsp:include page="footer.jsp" />