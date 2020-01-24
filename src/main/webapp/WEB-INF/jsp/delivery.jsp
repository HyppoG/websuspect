<!-- include header -->
<jsp:include page="header.jsp" />

<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"
	isELIgnored="false"%>
<c:url var="addDeliveryUrl" value="/addDelivery"></c:url>
<!-- body content -->
<div id="indexMain" class="container">

	<br>
	<c:if test="${not empty successMessage}">
		<div class="col-xs-12 alert alert-success" role="alert">${successMessage}
		</div>
	</c:if>
	<c:if test="${not empty errorMessage}">
		<div class="col-xs-12 alert alert-danger" role="alert">${errorMessage}</div>
	</c:if>

	<div class="multi_step_form">
		<form:form id="msform" modelAttribute="deliveryDetails"
			onsubmit="return validation();" action="${addDeliveryUrl}"
			method="post" class="need-validation">
			<!-- Tittle -->
			<div class="tittle">
				<h2>Delivery</h2>
				<p>Where do we ship to ?</p>
			</div>
			<!-- progressbar -->
			<ul id="progressbar">
				<li class="active">Delivery</li>
				<li>Shipping</li>
				<li>Payment</li>
				<li>Confirmation</li>
			</ul>

			<div class="col-xs-6 col-sm-6 col-md-6 col-lg-6 offset-sm-3">
				<input type="hidden" name="${csrfTokenParam}" value="${csrfToken}" />

				<div class="form-group row">
					<c:set var="houseNumberError"><form:errors path="houseNumber" class="invalid-feedback d-block" /></c:set>
					<label for="houseNumber" class="col-sm-5 col-form-label">House
						Number <sup>*</sup>
					</label>
					
					<div class="col-sm-7">
					<form:input type="text"
						class="form-control"
						path="houseNumber" id="houseNumber" placeholder="House Number"
						maxLength="30" />
					<div id="houseNumber_error">
						${houseNumberError}
					</div>
					</div>

				</div>

				<div class="form-group row">
					<c:set var="streetError"><form:errors id="street_error" path="street" class="invalid-feedback d-block" /></c:set>
					<label for="street" class="col-sm-5 col-form-label">Street <sup>*</sup>
					</label>
					<div class="col-sm-7">
					<form:textarea class="form-control" path="street" id="street"
						placeholder="Street" rows="2" cols="6"></form:textarea>
					${streetError}
					</div>
				</div>

				<i>Note : We currently only deliver to Gouda city</i></br>
				<div class="form-group row">
					<c:set var="cityError"><form:errors id="city_error" path="city" class="invalid-feedback d-block" /></c:set>
					<label for="city" class="col-sm-5 col-form-label">City <sup>*</sup>
					</label>
					<div class="col-sm-7">
					<form:input type="text" class="form-control" path="city" id="city"
						placeholder="City" maxLength="30" />
					${cityError}
					</div>

				</div>

				<div class="form-group row">
					<c:set var="countryError"><form:errors id="country_error" path="country" class="invalid-feedback d-block" /></c:set>
					<label for="country" class="col-sm-5 col-form-label">Country <sup>*</sup>
					</label>
					<div class="col-sm-7">
					<form:input type="text" class="form-control" path="country"
						id="country" placeholder="Country" maxLength="30" />
					${countryError}
					</div>
				</div>

				<br />
				<button onclick="history.back()"
					class="action-button previous_button">Previous</button>
				<button type="submit" class="next action-button">Continue</button>
			</div>

		</form:form>
	</div>
</div>
<!-- End body content -->

<script type="text/javascript">
	function validation() {
		isValidHN = checkForEmpty('houseNumber', 'houseNumber_error',
				'House Number');
		isValidStreet = checkForEmpty('street', 'street_error', 'Street');
		isValidCity = checkForEmpty('city', 'city_error', 'City');
		isValidCountry = checkForEmpty('country', 'country_error', 'country');
		if (isValidHN && isValidStreet && isValidCity && isValidState
				&& isValidCountry)
			return true;
		return false;
	}
</script>
<!-- include footer -->
<jsp:include page="footer.jsp" />