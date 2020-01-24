package com.test.vulnerableapp.controller.admin;

import com.test.vulnerableapp.model.OrderDetails;
import com.test.vulnerableapp.model.UserInfo;
import com.test.vulnerableapp.operations.UserOperation;
import com.test.vulnerableapp.operations.OrderManagerOperation;
import com.test.vulnerableapp.util.AppConstants;
import com.test.vulnerableapp.util.AppUtil;
import org.apache.log4j.Logger;
import org.owasp.esapi.ESAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Controller
public class AdminOrdersController {
	private static final Logger logger = Logger.getLogger(AdminOrdersController.class);
	@Autowired
    OrderManagerOperation orderManagerOperation;
	@Autowired
	private UserOperation userOperation;
	
	/**
	 * Method is used to render the User Owner Page
	 * @param model
	 * @return
	 */
	@RequestMapping(value = {"/admin/orders","/admin/orders/search"}, method = RequestMethod.GET)	
	public String viewOwnerHomePage(Model model) { 
		return AppConstants.URL_ADMIN_ORDERS;
	}
	
	/**
	 * This method is to handle the search order details
	 * @param model
	 * @param request
	 * @param redir
	 * @return
	 */
	@RequestMapping(value = {"/admin/orders/search"}, method = RequestMethod.POST)	
	public String searchOrder(Model model, HttpServletRequest request, RedirectAttributes redir) { 
		long generatedOrderId=0;
		try{
			generatedOrderId = Long.parseLong(request.getParameter("orderId"));
		}catch(NumberFormatException ex){
			logger.error("Order id is not a valid number.", ex);
			redir.addFlashAttribute("errorMessage", "Please Enter a valid order id.");
			return "redirect:"+AppConstants.URL_ADMIN_ORDERS;
		}
		OrderDetails orderDetails = getOrderDetails(generatedOrderId);
		if(orderDetails==null){
			model.addAttribute("errorMessage", "No Order found with this Order Id.");
		}else{
			orderDetails = getHTMLEncodedOrderDetails(orderDetails);
			model.addAttribute("orderDetails", orderDetails);
			model.addAttribute("cardNumber", AppUtil.getCardNumberToView(orderDetails.getPaymentDetails().getCardNumber()));
		}
		return AppConstants.URL_ADMIN_ORDERS;
	}
	
	private OrderDetails getOrderDetails(long generatedOrderId){
		OrderDetails dbOrderDetails = orderManagerOperation.getOrderDetails(generatedOrderId);
		if(dbOrderDetails == null) {
			return null;
		}
		Optional<UserInfo> user = userOperation.retrieveById(dbOrderDetails.getUserId());
		try {
			dbOrderDetails.getPaymentDetails().setCardNumber(AppUtil.decrypt(dbOrderDetails.getPaymentDetails().getCardNumber()));
		} catch (Exception e) {
			logger.error("Exception during the decryption of credit card information.", e);
		}
		dbOrderDetails.setUserName(user.get().getUsername());
		/*System.out.println("generated id "+ dbOrderDetails.getGeneratedOrderId());
		System.out.println("Number of Flight "+dbOrderDetails.getOrderedFlights().size());
		System.out.println("Flight Shipping"+dbOrderDetails.getShippingDetails().getType());
		System.out.println("City to Delivery "+dbOrderDetails.getDeliveryDetails().getCity());
		System.out.println("Payment card number "+dbOrderDetails.getPaymentDetails().getCardNumber());
		*/
		return dbOrderDetails;
	}
	
	/**
	 * It will return the Order information after HTML encoding (secured from stored XSS)
	 * @param orderDetails
	 * @return
	 */
	private OrderDetails getHTMLEncodedOrderDetails(OrderDetails orderDetails){
		orderDetails.setUserName(ESAPI.encoder().encodeForHTML(orderDetails.getUserName()));
		orderDetails.setDeliveryDetails(AppUtil.getHTMLEncodedDeliveryDetails(orderDetails.getDeliveryDetails()));
		orderDetails.setPaymentDetails(AppUtil.getHTMLEncodedPaymentDetails(orderDetails.getPaymentDetails()));
		orderDetails.setShippingDetails(AppUtil.getHTMLEncodedShippingDetails(orderDetails.getShippingDetails()));
		orderDetails.setOrderedFlights(AppUtil.getHTMLEncodedOrderedFlightsInfo(orderDetails.getOrderedFlights()));
		return orderDetails;
	}
}