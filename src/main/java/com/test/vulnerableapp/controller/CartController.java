package com.test.vulnerableapp.controller;

import static java.lang.Math.toIntExact;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.test.vulnerableapp.model.OrderDetails;
import com.test.vulnerableapp.model.OrderedFlightInfo;
import com.test.vulnerableapp.util.AppConstants;
import com.test.vulnerableapp.util.AppUtil;
@Controller
public class CartController{
	private static final Logger logger = LoggerFactory.getLogger(CartController.class);
	
	/**
	 * Method is used to render the User Order Form
	 * @param model
	 * @return
	 */
	@RequestMapping(value = {"/cart"}, method = RequestMethod.GET)	
	public String viewCartPage(Model model, HttpServletRequest request) { 
		HttpSession session = request.getSession(false);
		OrderDetails orderDetails =AppUtil.getOrderDetailsFromSession(session);
		
		if(orderDetails==null || !AppUtil.isOrderHasFlight(orderDetails)){
			model.addAttribute("successMessage", "There is no item in your cart. Please go back and add some flights to your cart !");
		} else {
			model.addAttribute("flightList", orderDetails.getOrderedFlights());
		}
		return AppConstants.URL_CART;
	}

	@RequestMapping(value = {"/updateCart"}, method = RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody String updateCart(@RequestBody OrderedFlightInfo orderedflighinfo, HttpServletRequest request, RedirectAttributes redir) {
		OrderDetails orderDetails = (OrderDetails)request.getSession(false).getAttribute("orderDetails");
		List<OrderedFlightInfo> orderedFlights;
		if(orderDetails==null) {
			return "{\"msg\":\"failed\"}";
		} else {
			orderedFlights = orderDetails.getOrderedFlights();
		}
		
		if(!validate(orderedflighinfo.getNumberOfPassengers())){
			redir.addFlashAttribute("errorMessage", AppConstants.MSG_INVALID_PRODUCT);
			return "{\"msg\":\"failed\"}";
		} else {
			OrderedFlightInfo orderedFlight = orderedFlights.get(toIntExact(orderedflighinfo.getFlightId())-1);
			orderedFlight.setNumberOfPassengers(orderedflighinfo.getNumberOfPassengers());
			orderedFlight.setPrice(orderedFlight.getPrice()*orderedflighinfo.getNumberOfPassengers());
			orderDetails.setOrderedFlights(orderedFlights);
			request.getSession(false).setAttribute("orderDetails", orderDetails);
			return "{\"msg\":\"success\"}";
		}
	}
	
	private boolean validate(int quantity){
		return AppUtil.isValidQuantity(quantity);
	}
}