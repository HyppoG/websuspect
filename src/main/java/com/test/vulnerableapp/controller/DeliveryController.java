package com.test.vulnerableapp.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import com.test.vulnerableapp.model.DeliveryDetails;
import com.test.vulnerableapp.model.OrderDetails;
import com.test.vulnerableapp.util.AppConstants;
import com.test.vulnerableapp.util.AppUtil;

@Controller
public class DeliveryController implements Validator {
	private static final Logger logger = LoggerFactory.getLogger(DeliveryController.class);
	
	/**
	 * Method is used to render the Delivery Form
	 * @param model
	 * @return
	 */
	@RequestMapping(value = {"/delivery"}, method = RequestMethod.GET)	
	public String showDeliveryPage(Model model, HttpServletRequest request, RedirectAttributes redir) { 
		HttpSession session = request.getSession(false);
		OrderDetails orderDetails =AppUtil.getOrderDetailsFromSession(session);
		if(orderDetails==null || !AppUtil.isOrderHasFlight(orderDetails)){
			redir.addFlashAttribute("errorMessage", AppConstants.MSG_INVALID_PRODUCT);
			return "redirect:"+AppConstants.URL_USER_HOME;
		}
		model.addAttribute("deliveryDetails", new DeliveryDetails());
		return AppConstants.URL_DELIVERY;
	}
	
	/**
	 * To add address information in the order
	 * @param deliveryDetails
	 * @param result
	 * @param model
	 * @param request
	 * @param redir
	 * @return
	 */
	@RequestMapping(value = "/addDelivery", method = RequestMethod.POST)
	public String addDeliveryDetails(@ModelAttribute(value="deliveryDetails") @Validated DeliveryDetails deliveryDetails, BindingResult result, 
			Model model,HttpServletRequest request, RedirectAttributes redir) {
		// validate user information
		HttpSession session = request.getSession(false);
		OrderDetails orderDetails = AppUtil.getOrderDetailsFromSession(session);
		if(orderDetails==null || !AppUtil.isOrderHasFlight(orderDetails)){
			redir.addFlashAttribute("errorMessage", AppConstants.MSG_INVALID_PRODUCT);
			return "redirect:"+AppConstants.URL_USER_HOME;
		}
		this.validate(deliveryDetails, result);
		
		if(result.hasErrors()){
			model.addAttribute("errorMessage", AppConstants.MSG_FORM_CONTAINS_EROOR);
			return AppConstants.URL_DELIVERY;
		}
		orderDetails.setDeliveryDetails(deliveryDetails);
		return "redirect:"+AppConstants.URL_SHIPPING;
	}
	
	// overridden methods from validator interface
	public boolean supports(Class<?> clazz) {
		return DeliveryDetails.class.isAssignableFrom(clazz);
	}

	/**
	 * validate the form inputs for Empty or invalid inputs
	 */
	public void validate(Object target, Errors errors) {
		DeliveryDetails deliveryBean = (DeliveryDetails)target;
		// Validation for Empty Fields
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "houseNumber", "required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "street", "required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "city", "required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "country", "required");

		if(!AppUtil.isValidInput("House Number", deliveryBean.getHouseNumber(), "HouseNumber", 30, false))
			errors.rejectValue("houseNumber","invalid");
		if(!AppUtil.isValidInput("Street", deliveryBean.getStreet(), "Name", 30, false))
			errors.rejectValue("street","invalid");
		if(!AppUtil.isValidInput("City", deliveryBean.getCity(), "Name", 30, false))
			errors.rejectValue("city","invalid");
		if(!AppUtil.isValidInput("Country", deliveryBean.getCountry(), "Name", 30, false))
			errors.rejectValue("country","invalid");
	}
}