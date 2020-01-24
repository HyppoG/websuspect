package com.test.vulnerableapp.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.owasp.esapi.errors.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.test.vulnerableapp.model.OrderDetails;
import com.test.vulnerableapp.model.OrderedFlightInfo;
import com.test.vulnerableapp.model.ShippingPrice;
import com.test.vulnerableapp.operations.OrderManagerOperation;
import com.test.vulnerableapp.util.AppConstants;
import com.test.vulnerableapp.util.AppUtil;
import com.test.vulnerableapp.util.ShippingXMLParser;
@Controller
public class ConfirmationController {
	private static final Logger logger = LoggerFactory.getLogger(ConfirmationController.class);
	@Value("${shippingXML}")
	private String SHIPPING_XML;
	@Value("${upload.location}")
	private String UPLOAD_DIR_PATH;
	@Autowired
	OrderManagerOperation orderManagerOperation;
	
	/**
	 * Method is used to render the Confirmation Form
	 * @param model
	 * @return
	 */
	@RequestMapping(value = {"/confirmation"}, method = RequestMethod.GET)	
	public String showConfirmationPage(Model model, HttpServletRequest request, RedirectAttributes redir) { 
		HttpSession session = request.getSession(false);
		OrderDetails orderDetails =AppUtil.getOrderDetailsFromSession(session);
		if(orderDetails==null || !AppUtil.isOrderHasFlight(orderDetails)){
			redir.addFlashAttribute("errorMessage", AppConstants.MSG_INVALID_PRODUCT);
			return "redirect:"+AppConstants.URL_USER_HOME;
		}
		if(orderDetails.getShippingDetails()==null ){
			redir.addFlashAttribute("errorMessage", AppConstants.MSG_INVALID_SHIPPING_INFO);
			return "redirect:"+AppConstants.URL_SHIPPING;
		}
		
		try {
			orderDetails.getPaymentDetails().setCardNumber(AppUtil.decrypt(orderDetails.getPaymentDetails().getCardNumber()));
			System.out.println(AppUtil.decrypt(orderDetails.getPaymentDetails().getCardNumber()));
		} catch (Exception e) {
			logger.error("Exception during the decryption of credit card information.", e);
		}
		model.addAttribute("cardNumber", AppUtil.getCardNumberToView(orderDetails.getPaymentDetails().getCardNumber()));
		return AppConstants.URL_CONFIRMATION;
	}
	
	/**
	 * Handle the remove flight request coming from confirmation page. 
	 * @param id
	 * @param model
	 * @param request
	 * @param redir
	 * @return
	 */
	@RequestMapping(value = "/removeFlight/{id}", method = RequestMethod.GET)
	public String removeFlightDetails(@PathVariable("id") long id, Model model, HttpServletRequest request, RedirectAttributes redir) {
		HttpSession session = request.getSession(false);
		OrderDetails orderDetails =AppUtil.getOrderDetailsFromSession(session);
		if(orderDetails==null || !AppUtil.isOrderHasFlight(orderDetails)){
			redir.addFlashAttribute("errorMessage", AppConstants.MSG_INVALID_PRODUCT);
			return "redirect:"+AppConstants.URL_USER_HOME;
		}

		List<OrderedFlightInfo> listOfFlights = orderDetails.getOrderedFlights();
		for(int i=0; i<listOfFlights.size();i++){
			OrderedFlightInfo flight = listOfFlights.get(i);
			if(flight.getFlightId()==id){
				listOfFlights.remove(i);
				break;
			}
		}
		
		model.addAttribute("orderDetails",orderDetails);
		model.addAttribute("cardNumber", AppUtil.getCardNumberToView(orderDetails.getPaymentDetails().getCardNumber()));
		return AppConstants.URL_CONFIRMATION;
	}
	
	/**
	 * When Order is confirmed then save the Order details in database.
	 * @param model
	 * @param request
	 * @param redir
	 * @return
	 * @throws ValidationException 
	 */
	@RequestMapping(value = "/confirmOrder", method = RequestMethod.GET)
	public String confirmOrderDetails(Model model, HttpServletRequest request, RedirectAttributes redir) throws ValidationException {
		HttpSession session = request.getSession(false);
		boolean isOrderSaved=false;
		OrderDetails orderDetails =AppUtil.getOrderDetailsFromSession(session);
		if(orderDetails==null || !AppUtil.isOrderHasFlight(orderDetails)){
			redir.addFlashAttribute("errorMessage", AppConstants.MSG_INVALID_PRODUCT);
			return "redirect:"+AppConstants.URL_USER_HOME;
		}
		double finalPrice = getFinalPriceForOrder(orderDetails);

		orderDetails.getPaymentDetails().setTotalAmount(finalPrice);
		// encrypt the credit card details before saving it to db
		String encrypt="";
		try {
			encrypt = AppUtil.encrypt(orderDetails.getPaymentDetails().getCardNumber());
		} catch (Exception e) {
			logger.error("Exception in encryption and decryption of Credit Card", e);
		}
		
		orderDetails.getPaymentDetails().setCardNumber(encrypt);
		// All details now will save in database
		isOrderSaved = orderManagerOperation.saveOrderDetailsInDb(orderDetails);
		if(!isOrderSaved){
			redir.addFlashAttribute("errorMessage", "There is some problem in the order you placed.");
		}else{
			session.removeAttribute("orderDetails");
			redir.addFlashAttribute("successMessage", "Your order is placed successfully.");
		}
		return "redirect:"+AppConstants.URL_USER_FEEDBACK;
	}
	
	/**
	 * Used to get shipping information using the city name.
	 * @param cityName
	 * @return
	 */
	public ShippingPrice getShippingRecord(String cityName){
		try {
			ShippingXMLParser shippingInfo = new ShippingXMLParser(SHIPPING_XML, UPLOAD_DIR_PATH);
			shippingInfo.setXmlFile(SHIPPING_XML);
			return shippingInfo.getShippingRecordByCity(cityName);
		} catch (Exception e){
			e.printStackTrace();
			return new ShippingPrice();
		}
	}
	
	/**
	 * Calculate the Final price which include the shipping price.
	 * @param orderDetails
	 * @return
	 */
	private double getFinalPriceForOrder(OrderDetails orderDetails){
		// Fixme + verify
		double shippingPrice = orderDetails.getShippingDetails().getPrice();
		double totalFlightPrice=0;
		for(OrderedFlightInfo flightInfo : orderDetails.getOrderedFlights()){
			totalFlightPrice = flightInfo.getPrice();
		}
		return totalFlightPrice + shippingPrice;
	}
}