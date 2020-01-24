package com.test.vulnerableapp.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.test.vulnerableapp.model.Flight;
import com.test.vulnerableapp.model.OrderDetails;
import com.test.vulnerableapp.model.OrderedFlightInfo;
import com.test.vulnerableapp.model.User;
import com.test.vulnerableapp.model.UserInfo;
import com.test.vulnerableapp.operations.FlightOperation;
import com.test.vulnerableapp.util.AppConstants;

/**
 *
 */
@Controller
public class OrderController {
	private static final Logger logger = LoggerFactory.getLogger(OrderController.class);
	@Autowired
	private FlightOperation flightService;

	/**
	 * Method is used to render the User Order Form
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = { "/order" }, method = RequestMethod.GET)
	public String viewOrderPage(Model model) {
		model.addAttribute("user", new UserInfo());
		model.addAttribute("flightList", getFlightList());
		return AppConstants.URL_USER_HOME;
	}

	/**
	 * This method is used to process flight's order from order page.
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = { "/setOrder" }, method = RequestMethod.POST)
	public String processOrder(Model model, HttpServletRequest request, HttpServletResponse response,
			RedirectAttributes redir) {
		User user = (User) request.getSession(false).getAttribute("user");
		OrderDetails orderDetails = (OrderDetails) request.getSession(false).getAttribute("orderDetails");
		List<OrderedFlightInfo> orderedFlights = new ArrayList<OrderedFlightInfo>();
		if (orderDetails != null) {
			orderedFlights = orderDetails.getOrderedFlights();
		} else {
			orderDetails = new OrderDetails();
		}

		OrderedFlightInfo orderedFlight = null;
		boolean isValidForm = true;
		double price = 0;
		int numberOfPassengers = 0;

		String date = request.getParameter("date");
		String from = request.getParameter("from");
		String to = request.getParameter("to");
		try {
			numberOfPassengers = Integer.parseInt(request.getParameter("numberOfPassengers"));
			price = Double.parseDouble(request.getParameter("price"));
		} catch (Exception e) {
			isValidForm = false;
			logger.error("Invalid number in quantity.", e);
		}

		if (isValidForm && validate(numberOfPassengers, date, from, to, price)) {
			orderedFlight = new OrderedFlightInfo();
			orderedFlight.setFrom(from);
			orderedFlight.setTo(to);
			orderedFlight.setPrice(numberOfPassengers * price);
			orderedFlight.setDate(date);
			orderedFlight.setNumberOfPassengers(numberOfPassengers);
			orderedFlight.setFlightId(orderedFlights.size() + 1);
			orderedFlights.add(orderedFlight);

			orderDetails.setUserId(user.getId());
			orderDetails.setOrderedFlights(orderedFlights);
			request.getSession(false).setAttribute("orderDetails", orderDetails);

			redir.addFlashAttribute("successMessage", AppConstants.MSG_FLIGHT_ADDED);
			return "redirect:/order";
		} else {
			redir.addFlashAttribute("errorMessage", AppConstants.MSG_INVALID_PRODUCT);
			return "redirect:/order";
		}

	}

	/**
	 * Validate the order form fields.
	 * 
	 * @return
	 */
	private boolean validate(int numberOfPassengers, String date, String from, String to, double price) {
		boolean isValidForm = true;
		// TODO : Add some validation...
		return isValidForm;
	}


	private List<Flight> getFlightList() {
		return flightService.retrieveAll();
	}

}