package com.test.vulnerableapp.controller;

import com.test.vulnerableapp.operations.UserOperation;
import com.test.vulnerableapp.util.AppConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
@Controller
public class HomeController {
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	@Autowired
	private UserOperation userOperation;

	/**
	 * Method to render the home page
	 * @param request
	 * @return
	 */
	@RequestMapping(value = {"/", "/home"} ,method = RequestMethod.GET)
	public String viewHomePage(HttpServletRequest request, Model model) {
		model.addAttribute("birthday", userOperation.getUserListByTodayDate());
		return AppConstants.URL_HOME;
	}
}