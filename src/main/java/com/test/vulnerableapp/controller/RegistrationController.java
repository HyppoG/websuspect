package com.test.vulnerableapp.controller;

import javax.servlet.http.HttpServletRequest;

import com.test.vulnerableapp.exception.CommonsException;
import com.test.vulnerableapp.operations.UserOperation;
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

import com.test.vulnerableapp.model.UserInfo;
import com.test.vulnerableapp.util.AppConstants;
import com.test.vulnerableapp.util.AppUtil;

@Controller
public class RegistrationController implements Validator {
	private static final Logger logger = LoggerFactory.getLogger(RegistrationController.class);

	@Autowired
	private UserOperation userOperation;


	/**
	 * Method is used to render the User Feedback Form
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = { "/registration", "/registerUser" }, method = RequestMethod.GET)
	public String viewRegistrationPage(Model model) {
		model.addAttribute("user", new UserInfo());
		model.addAttribute("register", true);
		return AppConstants.URL_LOGIN;
	}

	/**
	 * Method used to save user information in database
	 * 
	 * @param user
	 * @param result
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/registerUser", method = RequestMethod.POST)
	public String AddUser(@ModelAttribute(value = "user") @Validated UserInfo user, BindingResult result, Model model,
			HttpServletRequest request, RedirectAttributes redir) {
		// validate user information
		this.validate(user, result);

		if (result.hasErrors()) {
			model.addAttribute("errorMessage", AppConstants.MSG_FORM_CONTAINS_EROOR);
			model.addAttribute("register", true);
			return AppConstants.URL_LOGIN;
		}

		String password = AppUtil.getEncryptedPassword(user.getPassword());
		user.setPassword(password);
		user.setStatus("ACTIVE");
		user.setRole_id(AppConstants.CUSTOMER_ROLE_ID);

		userOperation.addUser(user);

		return "redirect:" + AppConstants.URL_LOGIN;
	}

	// overridden methods from validator interface
	public boolean supports(Class<?> clazz) {
		return UserInfo.class.isAssignableFrom(clazz);
	}

	public void validate(Object target, Errors errors) {
		UserInfo userBean = (UserInfo) target;

		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "username.required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "email.required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "password.required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password2", "password2.required");

		// Injection flaws - Other
		if (!userBean.getEmail().contains("@")) {
			errors.rejectValue("email", "email.invalid");
		}

		if (!userBean.getPassword().equals(userBean.getPassword2()))
			errors.rejectValue("password2", "password2.notmatch");
	}
}