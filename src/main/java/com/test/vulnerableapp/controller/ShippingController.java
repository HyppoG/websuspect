package com.test.vulnerableapp.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.test.vulnerableapp.model.OrderDetails;
import com.test.vulnerableapp.model.ShippingDetails;
import com.test.vulnerableapp.model.ShippingPrice;
import com.test.vulnerableapp.util.AppConstants;
import com.test.vulnerableapp.util.AppUtil;
import com.test.vulnerableapp.util.ShippingXMLParser;

@Controller
public class ShippingController {
	private static final Logger logger = LoggerFactory.getLogger(ShippingController.class);

	@Value("${shippingXML}")
	private String SHIPPING_XML;
	@Value("${upload.location}")
	private String UPLOAD_DIR_PATH;

	/**
	 * Method is used to render the Shipping Form
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = { "/shipping" }, method = RequestMethod.GET)
	public String showShippingPage(Model model, HttpServletRequest request, RedirectAttributes redir) {
		HttpSession session = request.getSession(false);
		OrderDetails orderDetails = AppUtil.getOrderDetailsFromSession(session);
		ShippingPrice shippingPrice = null;
		if (orderDetails == null || !AppUtil.isOrderHasFlight(orderDetails)) {
			redir.addFlashAttribute("errorMessage", AppConstants.MSG_INVALID_PRODUCT);
			return "redirect:" + AppConstants.URL_USER_HOME;
		}
		if (orderDetails.getDeliveryDetails() == null) {
			redir.addFlashAttribute("errorMessage", AppConstants.MSG_INVALID_DELIVERY_INFO);
			return "redirect:" + AppConstants.URL_DELIVERY;
		}

		// checks the xml file containing shipping price exist or not.
		try {
			shippingPrice = getShippingRecord(orderDetails.getDeliveryDetails().getCity());
		} catch (Exception e){
			model.addAttribute("errorMessage", "Shipping information is not found for this city.\n" + e.toString());
			model.addAttribute("deliveryDetails", orderDetails.getDeliveryDetails());
			return AppConstants.URL_DELIVERY;
		}

		if (shippingPrice.getShippingType() == null) {
			model.addAttribute("errorMessage", "Shipping information is not found for this city.");
			model.addAttribute("deliveryDetails", orderDetails.getDeliveryDetails());
			return AppConstants.URL_DELIVERY;
		}
		model.addAttribute("shippingDetails", new ShippingDetails());
		model.addAttribute("shippingPrice", shippingPrice.getShippingType());
		return AppConstants.URL_SHIPPING;
	}

	/**
	 * This will handle the request of adding the shipping record to order.
	 * 
	 * @param shippingDetails
	 * @param result
	 * @param model
	 * @param request
	 * @param redir
	 * @return
	 */
	@RequestMapping(value = "/addShipping", method = RequestMethod.POST)
	public String addShippingDetails(
			@ModelAttribute(value = "shippingDetails") @Validated ShippingDetails shippingDetails, BindingResult result,
			Model model, HttpServletRequest request, RedirectAttributes redir) {
		// validate user information
		HttpSession session = request.getSession(false);
		OrderDetails orderDetails = AppUtil.getOrderDetailsFromSession(session);
		if (orderDetails == null || !AppUtil.isOrderHasFlight(orderDetails)) {
			redir.addFlashAttribute("errorMessage", AppConstants.MSG_INVALID_PRODUCT);
			return "redirect:" + AppConstants.URL_USER_HOME;
		}

		if (shippingDetails.getType().isEmpty()) {
			model.addAttribute("errorMessage", AppConstants.MSG_FORM_CONTAINS_EROOR);
			return AppConstants.URL_SHIPPING;
		}

		// Validate the Shipping Type or Shipping service name
		if (!AppUtil.isValidShippingType(shippingDetails.getType())) {
			model.addAttribute("errorMessage", AppConstants.MSG_FORM_CONTAINS_EROOR);
			return AppConstants.URL_SHIPPING;
		}

		ShippingPrice shippingPrice = getPriceByCityAndShippingName(orderDetails.getDeliveryDetails().getCity(),
				shippingDetails.getType());
		double price = 0l;
		try {
			price = Double.parseDouble(shippingPrice.getShippingType().get(shippingDetails.getType()));
		} catch (NullPointerException e) {
			price = 0.0;
		}

		if (price <= 0) {
			model.addAttribute("errorMessage", AppConstants.MSG_FORM_CONTAINS_EROOR);
			return AppConstants.URL_SHIPPING;
		}

		shippingDetails.setPrice(price);
		orderDetails.setShippingDetails(shippingDetails);
		return "redirect:" + AppConstants.URL_PAYMENT;
	}

	/**
	 * Return the Shipping Record using city name by parsing the xml file
	 * 
	 * @param cityName
	 * @return
	 */
	public ShippingPrice getShippingRecord(String cityName) throws Exception {
		try {
			ShippingXMLParser shippingInfo = new ShippingXMLParser(SHIPPING_XML, UPLOAD_DIR_PATH);
			ShippingPrice shipping = shippingInfo.getShippingRecordByCity(cityName);
			if (shipping == null)
				return new ShippingPrice();
			return shipping;
		} catch (Exception e){
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * Return the Shipping Record using city name and shipping type by parsing the
	 * xml file.
	 * 
	 * @param cityName
	 * @return
	 */
	public ShippingPrice getPriceByCityAndShippingName(String cityName, String shippingName) {
		try {
			ShippingXMLParser shippingInfo = new ShippingXMLParser(SHIPPING_XML, UPLOAD_DIR_PATH);
			ShippingPrice shipping = shippingInfo.getPriceByCityAndShippingName(cityName, shippingName);
			if (shipping == null)
				return new ShippingPrice();
			return shipping;
		} catch (Exception e){
			e.printStackTrace();
			return new ShippingPrice();
		}
	}

}