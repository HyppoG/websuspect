package com.test.vulnerableapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.test.vulnerableapp.util.AppConstants;

@Controller
public class AboutUsController {
	/**
	 * Method is used to render the About Us Page
	 * @param model
	 * @return
	 */
	@RequestMapping(value = {"/about_us"}, method = RequestMethod.GET)	
	public String viewAboutUsPage(Model model) { 
		return AppConstants.URL_ABOUT_US;
	}
}