<!-- include header -->
<jsp:include page="header.jsp" />

<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"
	isELIgnored="false"%>
<c:url var="deliveryURL" value="/delivery" />
<c:url var="removeURL" value="/removeFlight" />
<!-- body content -->
<div id="indexMain" class="container">

	<c:if test="${not empty successMessage}">
		<div class="col-xs-12 alert alert-success" role="alert">${successMessage}
		</div>
	</c:if>
	<c:if test="${not empty errorMessage}">
		<div class="col-xs-12 alert alert-danger" role="alert">${errorMessage}</div>
	</c:if>

	<c:if test="${not empty flightList}">

	<div class="row">
		<div class="col-sm-offset-2 col-sm-10">
			<p>
				<strong>Shopping Cart</strong>
			</p>
			<br>

			<div class="jumbotron text-center">
				<table class="table table-striped">
					<!--Table head-->
					<thead>
						<tr>
							<th>From</th>
							<th>To</th>
							<th>Date</th>
							<th>Passengers</th>
							<th>Total Price</th>
							<th>Action</th>
						</tr>
					</thead>
					<!--Table head-->
					<!--Table body-->
					<tbody>
						<c:forEach items="${flightList}" var="flight">
							<tr>
								<td><b>${flight.from}</b></td>
								<td><b>${flight.to}</b></td>
								<td>${flight.date}</td>
								<td><select id="${flight.flightId}" class="selectPassenger"	onchange="updateCart(${flight.flightId})">
									<c:forEach begin="1" end="10" var="item">
										<option value="${item}"
											${item == flight.numberOfPassengers ? 'selected="selected"' : ''}>${item}</option>
									</c:forEach>
								</select></td>
								<td>${flight.price}euro</td>
								<td align="center"><a
									href="${removeURL}/${orderedProduct.productId}"
									class="btn btn-primary btn-xs">Remove</a></td>
							</tr>
							<c:set var="sum" value="${sum + flight.price}" />
						</c:forEach>
						<tr>
							<td colspan="4"><b>Total</b></td>
							<td class="text-right"><b>${sum} euro</b></td>
							<td></td>
						</tr>
					</tbody>
					<!--Table body-->
				</table>

				<a href="${deliveryURL}"><button type="button"
						class="btn btn-warning btn-rounded">Proceed to checkout</button></a>
			</div>
		</div>
	</div>

	</c:if>
</div>
<!-- End body content -->
<script type="text/javascript">
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
		}else{
			alert("Please select any one Shipping.");

		}
	}
</script>
<!-- include footer -->
<jsp:include page="footer.jsp" />