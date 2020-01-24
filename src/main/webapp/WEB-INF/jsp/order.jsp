<!-- include header -->
<jsp:include page="header.jsp" />  

<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored ="false" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!-- body content -->

<div id="indexMain" class="container-fluid">	
    <c:if test="${not empty successMessage}">
      <div class="col-xs-12 alert alert-success" role="alert">${successMessage} </div>
    </c:if>
    <c:if test="${not empty errorMessage}">
      <div class="col-xs-12 alert alert-danger" role="alert">${errorMessage}</div>
    </c:if>
       <input type="hidden" name="${csrfTokenParam}" value="${csrfToken}" />
       
       <div id="indexMain" class="container-fluid">
       	<div class="row pb-5">
       		<!--Flight cards-->
        	<c:forEach items="${flightList}" var="flight">
        		<c:url value="/images?file=${fn:replace(flight.imageUrl,' ','_')}" var="imageUrl"> </c:url>
	        
	            <!--Grid column-->
	            <div class="col-lg-5 col-xl-5 pb-3">
	                <!--Featured image-->
	                <div class="view overlay rounded z-depth-2">
	                    <img src="${imageUrl}" alt="photo of destination" class="img-fluid">
	                    <a>
	                        <div class="mask waves-effect waves-light"></div>
	                    </a>
	                </div>
	            </div>
	
	            <!--Grid column-->
	            <div class="col-lg-7 col-xl-7">
	                <!--Excerpt-->
	                <h3 class="mb-4 font-weight-bold dark-grey-text">
	                    <strong>${flight.from} to ${flight.to}</strong>
	                </h3>
	                <p>
	                    Nemo enim ipsam voluptatem quia voluptas sit aspernatur aut odit aut fugit, sed quia consequuntur magni
	                    dolores eos qui ratione voluptatem sequi nesciunt. Neque porro qui dolorem ipsum quia sit amet, consectetur.
	                </p>
	                <p>
	                    <strong>Departure Time: </strong> ${flight.departureTime}
	                </p>
	                <p>
	                    <strong>Price per person: </strong> ${flight.price} euro
	                </p>
	                <!-- <form class="form-inline" name="orderForm" id="orderForm" action="setOrder" method="post" onsubmit="return validate();">  -->
	                <form class="form-inline" name="orderForm" id="orderForm" action="setOrder" method="post">
	                    <div class="md-form">
	                        Passengers (max 10): <input type="text" style="width:50px;" class="form-control mb-2 mr-sm-2 numPassengers" id="${flight.id}" data-price="${flight.price}" name="numberOfPassengers" required>
	                    </div>
	                    <div class="md-form" style="margin-left:2%;">
	                        <input data-provide="datepicker" data-date-format="dd/mm/yyyy" class="form-control datepicker" name="date" placeholder="date of departure" required>
	                    </div>
	                    <div class="md-form" hidden>
	                        <input type="text" class="form-control mb-2 mr-sm-2" name="from" value="${flight.from}" hidden>
	                    </div>
	                    <div class="md-form" hidden>
	                        <input type="text" class="form-control mb-2 mr-sm-2" name="to" value="${flight.to}" hidden>
	                    </div>
	                    <div class="md-form" hidden>
	                        <input type="text" class="form-control mb-2 mr-sm-2" id="price${flight.id}" value="${flight.price}" name="price" hidden>
	                    </div>
	                    <div class="md-form" style="margin-left:2%;">
	                        <span id="total${flight.id}"></span>
	                    </div>
	                    <button id="submitform" style="margin-left: 2%; margin-bottom: 0px;" type="submit" class="btn btn-indigo btn-md waves-effect waves-light">Book this flight</button>
	                </form>
	            </div>
	            <!--Grid column-->
	    	</c:forEach>
        </div>
    </div>
<!-- End body content -->
<script>
    function validate(){
        isValidFields = true;
        isValidForm = true;
        var table = document.getElementById("orderTable");
        var totalRows = table.rows.length;
        for(i=1; i<totalRows; i++){
            resetError(i);
            flight = document.getElementById("flight_"+i).value;
            size = document.getElementById("size_"+i).value;
            color = document.getElementById("color_"+i).value;
            //alert("quantity_"+i);
            quantity = document.getElementById("quantity_"+i).value;
            //alert(quantity);
            if(quantity>0 && quantity<=50){
                isValidFields = isValidColorAndSize(i);
            }else if(size!='Select' || color!='Select'){
                isValidFields = isValidColorAndSize(i);
                if(quantity<=0 || quantity>50){
                    isValidFields=false;
                    showQuantityError('Quantity', 'quantity_'+i+'_error');
                }
            }else if(quantity<0 || quantity>50){
                isValidFields=false;
                showQuantityError('Quantity', 'quantity_'+i+'_error');
            }
            if(!isValidFields)isValidForm = isValidFields;
        }
        return isValidForm;
    }
    
    function isValidColorAndSize(index){
        isValid = true;
        if(size=='Select'){
            isValid = false;
            showError('Size', 'size_'+index+'_error');
        }
        if(color=='Select'){
            isValid = false;
            showError('Color', 'color_'+index+'_error');
        }
        return isValid;
    }
    
    function showError(fieldText, field){
        var fieldError = document.getElementById(field);
        fieldError.innerHTML="Select valid "+fieldText+".";

    }
    
    function showQuantityError(fieldText, field){
        var fieldError = document.getElementById(field);
        fieldError.innerHTML="Quantity should be between 0 to 50.";

    }
    
    function resetError(index){
        document.getElementById("size_"+index+"_error").innerHTML="";
        document.getElementById("color_"+index+"_error").innerHTML="";
        document.getElementById("quantity_"+index+"_error").innerHTML="";

    }
    
    function seeInfo() {
        var panel = document.getElementById("info");
        if (panel.style.display === "none") {
            panel.style.display = "block";
        } else {
            panel.style.display = "none";
        }
    }
</script>
<!-- include footer -->    
<jsp:include page="footer.jsp" />