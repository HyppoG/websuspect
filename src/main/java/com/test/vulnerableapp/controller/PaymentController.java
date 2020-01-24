package com.test.vulnerableapp.controller;

import com.test.vulnerableapp.model.OrderDetails;
import com.test.vulnerableapp.model.PaymentDetails;
import com.test.vulnerableapp.operations.OrderOperation;
import com.test.vulnerableapp.util.AppConstants;
import com.test.vulnerableapp.util.AppUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Controller
public class PaymentController  implements Validator{
	private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);
	@Autowired
	private OrderOperation orderOperation;
	
	/**
	 * Method is used to render the Payment Form
	 * @param model
	 * @return
	 */
	@RequestMapping(value = {"/payment"}, method = RequestMethod.GET)	
	public String showPaymentPage(Model model, HttpServletRequest request, RedirectAttributes redir) { 
		HttpSession session = request.getSession(false);
		OrderDetails orderDetails =AppUtil.getOrderDetailsFromSession(session);
		if(orderDetails==null || !AppUtil.isOrderHasFlight(orderDetails)){
			redir.addFlashAttribute("errorMessage", AppConstants.MSG_INVALID_PRODUCT);
			return "redirect:"+AppConstants.URL_USER_HOME;
		}
		if(orderDetails.getShippingDetails()==null){
			redir.addFlashAttribute("errorMessage", AppConstants.MSG_INVALID_SHIPPING_INFO);
			return "redirect:"+AppConstants.URL_SHIPPING;
		}
		model.addAttribute("paymentDetails", new PaymentDetails());
		return AppConstants.URL_PAYMENT;
	}
	
	/**
	 * Add payment details in the Flight Order
	 * @param paymentDetails
	 * @param result
	 * @param model
	 * @param request
	 * @param redir
	 * @return
	 */
	@RequestMapping(value = "addPayment", method = RequestMethod.POST)
	public String addPaymentDetails(@ModelAttribute(value="paymentDetails") @Validated PaymentDetails paymentDetails, BindingResult result, 
			Model model,HttpServletRequest request, RedirectAttributes redir) {
		// validate user information
		this.validate(paymentDetails, result);
		
		if(result.hasErrors()){
			model.addAttribute("errorMessage", AppConstants.MSG_FORM_CONTAINS_EROOR);
			return AppConstants.URL_PAYMENT;
		}
		HttpSession session = request.getSession(false);
		OrderDetails orderDetails =AppUtil.getOrderDetailsFromSession(session);
		if(orderDetails==null || !AppUtil.isOrderHasFlight(orderDetails)){
			redir.addFlashAttribute("errorMessage", AppConstants.MSG_INVALID_PRODUCT);
			return "redirect:"+AppConstants.URL_USER_HOME;
		}
		String encrypt="";
		try {
			encrypt = AppUtil.encrypt(paymentDetails.getCardNumber());
		} catch (Exception e) {
			logger.error("Exception in encryption and decryption of Credit Card", e);
		}
		paymentDetails.setCardNumber(encrypt);
		orderDetails.setPaymentDetails(paymentDetails);
		
		long generatedOrderId = 0;
		// checks the duplicate order id in db 
		do{
			generatedOrderId = AppUtil.getGeneratedOrderId();
			
		}while(isDuplicateOrderId(generatedOrderId));
		
		orderDetails.setGeneratedOrderId(generatedOrderId);
		return "redirect:"+AppConstants.URL_CONFIRMATION;
	}
	
	// create the type of credit card list
	@ModelAttribute("cardTypes")
	private List<String> getAllCardTypes(){
		List<String> cardList = new ArrayList<String>();
		cardList.add(AppConstants.CARD_MASTER);
		cardList.add(AppConstants.CARD_VISA);
		cardList.add(AppConstants.CARD_AMEX);
		return cardList;
	}
	
	// overridden methods from validator interface
	public boolean supports(Class<?> clazz) {
		return PaymentDetails.class.isAssignableFrom(clazz);
	}

	/**
	 * validate the form inputs for Empty or invalid inputs
	 */
	public void validate(Object target, Errors errors) {
		PaymentDetails paymentBean = (PaymentDetails)target;
		int currentYear, expiryYear, currentMonth, expiryMonth;
		boolean isValidDateFormat=true;
		boolean isValidCVVFormat=true;
		
		// Get current Year and Month
		currentYear = Calendar.getInstance().get(Calendar.YEAR);
		currentMonth = Calendar.getInstance().get(Calendar.MONTH);

		// Validation for Empty Fields
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "cardOwner", "required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "cardNumber", "required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "expiryMonth", "required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "expiryYear", "required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "cvvNumber", "required");
		
		if("Select".equalsIgnoreCase(paymentBean.getCardType()))
			errors.rejectValue("cardType","required");
		
		// Validation for valid input types
		try {
			if(!AppUtil.isValidInput("Card Owner", paymentBean.getCardOwner(), "Name", 30, false))
				errors.rejectValue("cardOwner","invalid");
			if(!AppUtil.isValidInput("Credit Card Number", paymentBean.getCardNumber(), "CreditCard", 16, false))
				errors.rejectValue("cardNumber","invalid");
			if(!AppUtil.isValidInput("Expiry Year", paymentBean.getExpiryYear(), "Number", 4, false)){
				isValidDateFormat=false;
				errors.rejectValue("expiryYear","invalid", AppUtil.encodeForHTML(paymentBean.getExpiryYear())+" is invalid Year.");
			}
			if(!AppUtil.isValidInteger("Expiry Month", paymentBean.getExpiryMonth(), 1, 12, false)){
				isValidDateFormat=false;
				errors.rejectValue("expiryMonth","invalid", AppUtil.encodeForHTML(paymentBean.getExpiryMonth())+" is invalid Month.");
			}
			if(!AppUtil.isValidInput("CVV Number", paymentBean.getCvvNumber(), "Number", 4, false)){
				isValidCVVFormat=false;
				errors.rejectValue("cvvNumber","invalid");
			}
			
			// Card expiry date should not less then current date
			if(isValidDateFormat){
				expiryYear = Integer.parseInt(paymentBean.getExpiryYear());
				expiryMonth = Integer.parseInt(paymentBean.getExpiryMonth());
				if(expiryYear<currentYear){
					errors.rejectValue("expiryYear","invalid", AppUtil.encodeForHTML(paymentBean.getExpiryYear())+" is invalid Year.");
				}else if(expiryYear==currentYear && expiryMonth<=currentMonth){
					errors.rejectValue("expiryMonth", "invalid", AppUtil.encodeForHTML(paymentBean.getExpiryMonth())+" is invalid Month.");
				}
			}
			
			// Check for valid length of cvv number for particular card type
			if(isValidCVVFormat){
				if(paymentBean.getCardType().equalsIgnoreCase("Amex") && paymentBean.getCvvNumber().length()!=4){
					errors.rejectValue("cvvNumber","invalid.Amex");
				}else if(!paymentBean.getCardType().equalsIgnoreCase("Amex") && paymentBean.getCvvNumber().length()!=3){
					errors.rejectValue("cvvNumber","invalid.masterCard");
				}
			}
		} catch (NumberFormatException e) {
			//e.printStackTrace();
			errors.rejectValue("expiryYear","invalid", AppUtil.encodeForHTML(paymentBean.getExpiryYear())+" is invalid Year.");
			errors.rejectValue("expiryMonth","invalid", AppUtil.encodeForHTML(paymentBean.getExpiryMonth())+" is invalid Month.");
			logger.debug("Invalid Credit Card expiry date. ", e);
		}
	}
	
	private boolean isDuplicateOrderId(long generatedOrderId){
		//return
		return orderOperation.existsById(generatedOrderId);
	}
}