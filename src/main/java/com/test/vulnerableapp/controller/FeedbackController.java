package com.test.vulnerableapp.controller;

import com.test.vulnerableapp.model.Feedback;
import com.test.vulnerableapp.operations.FeedbackOperation;
import com.test.vulnerableapp.util.AppConstants;
import com.test.vulnerableapp.util.AppUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.ValidationException;
@Controller
public class FeedbackController implements Validator{
	private static final Logger logger = LoggerFactory.getLogger(FeedbackController.class);

	@Autowired
	private FeedbackOperation feedbackOperation;
	
	/**
	 * Method is used to render the User Feedback Form
	 * @param model
	 * @return
	 */
	@RequestMapping(value = {"/feedback","/feedback/add"}, method = RequestMethod.GET)	
	public String viewFeedbackReport(Model model) { 
		model.addAttribute("feedback", new Feedback());
		return AppConstants.URL_USER_FEEDBACK;
	}
	
	/**
	 * Method used to save user information in database
	 * @param feedback
	 * @param result
	 * @return
	 */
	@RequestMapping(value = "/feedback/add", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> AddFeedback(@RequestBody Feedback feedback, BindingResult result) {
		// validate user information
		this.validate(feedback, result);
		
		if(result.hasErrors()){
			return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
		}
		
		// save feedback information to database
		feedbackOperation.addFeedback(feedback);
		return new ResponseEntity<Object>(HttpStatus.OK);
	}

	// overridden methods from validator interface
	public boolean supports(Class<?> clazz) {
		return Feedback.class.isAssignableFrom(clazz);
	}

	/**
	 * validate the form inputs for Empty or invalid inputs
	 */
	public void validate(Object target, Errors errors) {
		Feedback feedbackBean = (Feedback)target;

		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "username.required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "email.required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "feedback", "feedback.required");

		try {
			if(!AppUtil.isValidInput("Email", feedbackBean.getEmail(), "Email", 70, false))errors.rejectValue("email","email.invalid");
		} catch (ValidationException e) {
			logger.debug("feedback form validation fail ", e);
		}
		if ( feedbackBean.getUsername() == null || feedbackBean.getUsername().contains("<script") ) { errors.rejectValue("username","username.invalid"); }
		if ( feedbackBean.getEmail() == null || feedbackBean.getEmail().contains("<script") ) { errors.rejectValue("email","email.invalid"); }
		if ( feedbackBean.getFeedback() == null || feedbackBean.getFeedback().contains("<script") ) { errors.rejectValue("feedback","feedback.invalid"); }
		
	}
}