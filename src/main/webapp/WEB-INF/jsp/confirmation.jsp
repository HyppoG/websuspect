<!-- include header -->
<jsp:include page="header.jsp" />  

<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored ="false" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!-- body content -->
<div id="indexMain" class="container-fluid" style="margin-bottom:15px;">	
	<c:if test="${not empty successMessage}">
		<div class="col-xs-12 alert alert-success" role="alert">${successMessage} </div>
	</c:if>
	<c:if test="${not empty errorMessage}">
		<div class="col-xs-12 alert alert-danger" role="alert">${errorMessage}</div>
	</c:if>
	  
	<div class="multi_step_form">  
		<div id="msform"> 
			<!-- Tittle -->
			<div class="tittle">
				<h2>Confirmation</h2>
				<p>Summary of order and details for ${username} - Your Order Number is <span>${orderDetails.generatedOrderId}</span>.</p>
			</div>
			<!-- progressbar -->
			<ul id="progressbar">
				<li class="active">Delivery</li>  
				<li class="active">Shipping</li> 
				<li class="active">Payment</li>
				<li class="active">Confirmation</li>
			</ul>
			
			<div class="row">
				 <table class="table table-striped">
				 	<thead class="thead-dark">
			          <tr>
			            <th scope="col">From</th>
                        <th scope="col">To</th>
                        <th scope="col">Date</th>
                        <th scope="col">Passengers</th>
                        <th scope="col">Total Price</th>
                        <th scope="col">Action</th>
			          </tr>
		          	</thead>
		          	<c:forEach items="${orderDetails.orderedFlights}" var="orderedFlight">
						<tr>
							<td scope="row">${orderedFlight.from}</td>
	                        <td scope="row">${orderedFlight.to}</td>
	                        <td>${orderedFlight.date}</td>
	                        <td>${orderedFlight.numberOfPassengers}</td>
	                        <td>${orderedFlight.price} euro</td>
	                        <td align="center">
								<a href="${removeURL}/${orderedFlight.flightId}" class="btn btn-primary btn-xs">Remove</a>
							</td>
						</tr>
						  <c:set var="sum" value="${sum + orderedFlight.price}"/>      
						</c:forEach>
						<tr class="table-warning">
							<td scope="row">Total</td>
							<td></td>
							<td></td>
							<td></td>
							<td>${sum} euro</td>
							<td></td>
						</tr>
						<tr class="table-warning">
							<td scope="row">Shipping Charges</td>
							<td></td>
							<td></td>
							<td></td>
							<td>${orderDetails.shippingDetails.price} euro</td>
							<td></td>
						</tr>
						<tr class="table-danger">
							<td scope="row">Final Cost</td>
							<td></td>
							<td></td>
							<td></td>
							<td>${sum+orderDetails.shippingDetails.price} euro</td>
							<td></td>
						</tr>
		          	</tbody>
				 </table>
			</div>
		 	<div class="row">
			 	<table class="table table-striped">
		 			<thead class="thead-dark">
			          <tr>
			            <th scope="col">Title</th>
			            <th scope="col">Details</th>
			          </tr>
		          	</thead>
		          	<tbody>
		          		<tr>
			            	<td>Delivery Address</td>
			            	<td>
			            	${orderDetails.deliveryDetails.houseNumber} &#44; ${orderDetails.deliveryDetails.street}<br>
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
			<div class="row">
			    <button onclick="history.back()" class="action-button previous_button">Previous</button>
				<c:url var="confirmationURL" value="/confirmOrder"/>
                <a href="${confirmationURL}" class="next action-button">Submit to Confirm</a>
			</div>
				
	   	</div>
	</div>
</div>
<!-- End body content -->

<!-- include footer -->    
<jsp:include page="footer.jsp" />