<!-- include header -->
<jsp:include page="../header.jsp" />

<%@ page contentType="text/html;charset=UTF-8" language="java"
	isELIgnored="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!-- body content -->

<div id="indexMain" class="container-fluid" style="margin-bottom: 15px;">

	<div class="row">
		<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12 text-center">
			<h2>Summary of order and details</h2>
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
			<c:url var="searchURL" value="/admin/orders/search" />
			<form class="form-horizontal" id="searchForm" action="${searchURL}"
				method="POST">
				<input type="hidden" name="${csrfTokenParam}" value="${csrfToken}" />
				<div class="form-group">
					<label class="form-label"></label>Search using Order Number<label></label>
					<input class="form-control" type="text" name="orderId" id="orderId" />
				</div>
				<div class="text-center">
					<button type="submit"
						class="btn btn-primary btn-group-justified btn-sm">Search</button>
				</div>
			</form>
		</div>
	</div>

	<c:if test="${not empty orderDetails}">
		<div class="container" style="margin-top: 20px;">
			<div class="row">
				<div class="col-xs-6 col-sm-6 col-md-6 col-lg-6 offset-sm-3">
					Order number <b>${orderDetails.generatedOrderId}</b> is from <b><span
						class="text-capitalize">${orderDetails.userName}</span></b>.
				</div>
			</div>
			<br>
			<div class="row">
				<div class="col-xs-6 col-sm-6 col-md-6 col-lg-6 offset-sm-3">
					<div class="table-responsive">
						<table class="table table-hover">
							<thead class="mdb-color info-color">
								<tr>
									<th scope="col">From</th>
			                        <th scope="col">To</th>
			                        <th scope="col">Date</th>
			                        <th scope="col">Passengers</th>
									<th scope="col">Price</th>
								</tr>
							</thead>
							<tbody>
							<c:forEach items="${orderDetails.orderedFlights}"
								var="orderedFlight">
								<tr>
									<td scope="row">${orderedFlight.from}</td>
			                        <td scope="row">${orderedFlight.to}</td>
			                        <td>${orderedFlight.date}</td>
			                        <td>${orderedFlight.numberOfPassengers}</td>
			                        <td>${orderedFlight.price} euro</td>
								</tr>
								<c:set var="sum" value="${sum + orderedFlight.price}" />
							</c:forEach>
							<tr class="table-warning">
								<td scope="row">Total</td>
								<td></td>
								<td></td>
								<td></td>
								<td>${sum} euro</td>
							</tr>
							<tr class="table-warning">
								<td scope="row">Shipping Charges</td>
								<td></td>
								<td></td>
								<td></td>
								<td>${orderDetails.shippingDetails.price} euro</td>
							</tr>
							<tr class="table-danger">
								<td scope="row">Final Cost</td>
								<td></td>
								<td></td>
								<td></td>
								<td>${sum+orderDetails.shippingDetails.price} euro</td>
							</tr>
							</tbody>
						</table>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-xs-6 col-sm-6 col-md-6 col-lg-6 offset-sm-3">
					<div class="table-responsive">
						<table class="table table-hover">
							<thead class="mdb-color info-color">
								<tr>
									<th>Title</th>
									<th>Details</th>
								</tr>
							</thead>
							<tbody>
								<tr>
									<td>Delivery Address</td>
									<td>${orderDetails.deliveryDetails.houseNumber} &#44;
										${orderDetails.deliveryDetails.street} <br>
										${orderDetails.deliveryDetails.city}&#44;
										${orderDetails.deliveryDetails.country}
									</td>
								</tr>
								<tr>
									<td>Card Details</td>
									<td>${cardNumber}</td>
								</tr>
							</tbody>
						</table>
					</div>
				</div>
			</div>
		</div>
	</c:if>
</div>
<!-- End body content -->
<!-- include footer -->
<jsp:include page="../footer.jsp" />