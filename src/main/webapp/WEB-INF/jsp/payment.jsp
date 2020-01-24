<!-- include header -->
<jsp:include page="header.jsp" />
 
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored ="false" %>
<c:url var="addPaymentURL" value="addPayment"></c:url>

<!-- body content -->
    <div id="indexMain" class="container">
    	<br> <c:if test="${not empty successMessage}">
		  <div class="col-xs-12 alert alert-success" role="alert">${successMessage} </div>
		</c:if>
		<c:if test="${not empty errorMessage}">
		  <div class="col-xs-12 alert alert-danger" role="alert">${errorMessage}</div>
		</c:if>    
	    
	    <div class="multi_step_form">  
			<form:form id="msform" modelAttribute="paymentDetails" onsubmit="return validate();" action="${addPaymentURL}" method="post"> 
				<!-- Tittle -->
				<div class="tittle">
					<h2>Payment</h2>
					<p>Please fill your payment details</p>
				</div>
				<!-- progressbar -->
				<ul id="progressbar">
					<li class="active">Delivery</li>  
					<li class="active">Shipping</li> 
					<li class="active">Payment</li>
					<li>Confirmation</li>
				</ul>
	    	
	    		<div class="offset-sm-3">
			      	<input type="hidden" name="${csrfTokenParam}" value="${csrfToken}" />
					<div class="form-group row">
						<c:set var="cardOwnerError"><form:errors path="cardOwner" class="invalid-feedback d-block" /></c:set>
			            <label for="cardOwner" class="col-form-label col-sm-5">Name on Card <sup>*</sup></label>
			            <div class="col-sm-7">
			                <form:input type="text" class="form-control" path="cardOwner" id="cardOwner" placeholder="Name on Card" maxLength="30"
			                onkeyup="checkForEmpty('cardOwner','cardOwner_error','Name on Card')"/>
			                ${cardOwnerError}
			            </div>
			        </div>
	        
			        <div class="form-group row">
			        <c:set var="cardNumberError"><form:errors path="cardNumber" class="invalid-feedback d-block" /></c:set>
			            <label for="cardNumber" class="col-form-label col-sm-5">Card Number <sup>*</sup></label>
			            <div class="col-sm-7">
			                <form:input type="text" class="form-control" path="cardNumber" id="cardNumber" placeholder="Card Number" maxLength="16"
			                onkeyup="checkForEmpty('cardNumber','cardNumber_error','Card Number')"/>
			                ${cardNumberError}
			            </div>
			        </div>
	        
			         <div class="form-group row">
			         <c:set var="cardTypeError"><form:errors path="cardType" class="invalid-feedback d-block" /></c:set>
			            <label for="cardType" class="col-form-label col-sm-5">Type of Card <sup>*</sup></label>
			            <div class="col-sm-7">
			                 <form:select class="form-control" path="cardType" id="cardType" 
			                 onchange="checkForEmpty('cardType','cardType_error','Type of Card')">
			                 	<option Value="Select">Select</option>
			                 	<!-- <option value="Mastercard">Mastercard</option>
			                 	<option value="Visa">Visa</option>
			                 	<option value="Amex">Amex</option> -->
			                 	<form:options items="${cardTypes}" />
			                 </form:select>
			                ${cardTypeError}
			            </div>
			        </div>
	
			        <div class="form-group row">
			        	<c:set var="expiryMonthError"><form:errors path="expiryMonth" class="invalid-feedback d-block" /></c:set>
			        	<c:set var="expiryYearError"><form:errors path="expiryYear" class="invalid-feedback d-block" /></c:set>
			            <label for="expiryMonth" class="col-form-label col-sm-5">Expiry Date <sup>*</sup></label>
			            <div class="col-sm-3">
			               <form:input type="text" class="form-control" path="expiryMonth" id="expiryMonth" placeholder="Month(eg. 5)" size="8" maxLength="30"
			               onkeyup="checkForEmpty('expiryMonth','expiryMonth_error','Expiry Date')"/>
			               ${expiryMonthError}
			            </div>
			            <div class="col-sm-3">
			               <form:input type="text" class="form-control" path="expiryYear" id="expiryYear" placeholder="Year(eg. 2015)" size="5" maxLength="30"
			               onkeyup="checkForEmpty('expiryYear','expiryYear_error','Expiry Year')"/>
			               ${expiryYearError}
			            </div>
			        </div>
	        
			        <div class="form-group row">
			        <c:set var="cvvNumberError"><form:errors path="cvvNumber" class="invalid-feedback d-block" /></c:set>
			            <label for="cvvNumber" class="col-form-label col-sm-5">CVV Number <sup>*</sup></label>
			            <div class="col-sm-7">
			                <form:input type="text" class="form-control" path="cvvNumber" id="cvvNumber" placeholder="CVV Number" maxLength="30"
			                onkeyup="checkForEmpty('cvvNumber','cvvNumber_error','CVV Number')"/>
			                ${cvvNumberError}
			            </div>
			        </div>
	
			        <button onclick="history.back()" class="action-button previous_button">Previous</button>
					<button type="submit" class="next action-button">Continue</button>
			    </div>
	    	</form:form>
	    </div>
	</div>
<!-- End body content -->

<script>
	function validate(){
		isValidForm = true;
		var cardOwner = document.getElementById("cardOwner").value;
		var cardNumber = document.getElementById("cardNumber").value;
		var cardType = document.getElementById("cardType").value;
		var expiryMonth = document.getElementById("expiryMonth").value;
		var expiryYear = document.getElementById("expiryYear").value;
		var cvvNumber = document.getElementById("cvvNumber").value;
		resetError();
		if(cardOwner.trim()==""){
			isValidForm=false;
			showError('Name on Card','cardOwner',false);
		} 
		if(cardNumber.trim()==""){
			isValidForm=false;
			showError('Card Number','cardNumber',false);
		}
		if(expiryMonth.trim()==""){
			isValidForm=false;
			showError('Expiry Month','expiryMonth',false);
		}
		if(expiryYear.trim()==""){
			isValidForm=false;
			showError('Expiry Year','expiryYear',false);
		}
		if(cardType.trim()=="Select"){
			isValidForm=false;
			showError('Type of Card', 'cardType', true);
		}
		if(cvvNumber.trim()==""){
			isValidForm=false;
			showError('CVV Number','cvvNumber',false);
		}
		return isValidForm;
	}
	
	function showError(fieldText, field, isSelect){
		field = field + "_error";
		var fieldError = document.getElementById(field);
		if(isSelect){
			fieldError.innerHTML="Select valid "+fieldText+".";
		}else{
			fieldError.innerHTML=fieldText+" id required";
		}

	}
	
	function resetError(){
		document.getElementById("cardOwner_error").innerHTML="";
		document.getElementById("cardNumber_error").innerHTML="";
		document.getElementById("cardType_error").innerHTML="";
		document.getElementById("expiryMonth_error").innerHTML="";
		document.getElementById("expiryYear_error").innerHTML="";
		document.getElementById("cvvNumber_error").innerHTML="";

	}
</script>
<!-- include footer -->
<jsp:include page="footer.jsp" />