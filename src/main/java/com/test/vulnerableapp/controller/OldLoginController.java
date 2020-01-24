package com.test.vulnerableapp.controller;

import com.test.vulnerableapp.model.User;
import com.test.vulnerableapp.operations.OldUserOperation;
import com.test.vulnerableapp.util.AppConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Controller
public class OldLoginController {
	private static final Logger logger = LoggerFactory.getLogger(OldLoginController.class);

	@Autowired
	private OldUserOperation userOperation;

	private String username;
	private String password;
	private String username_error;
	private String password_error;	
	private Map<String,String> errors;
	 
	/**
	 * Method to render the login page
	 * @param request
	 * @return
	 */
	@RequestMapping(value = {"/oldlogin" } ,method = RequestMethod.GET)
	public String viewLoginPage(HttpServletRequest request) {
		//logs debug message
		if(logger.isDebugEnabled()){
			logger.debug("User Controller is executed!");
		}
		HttpSession session = request.getSession(false);
		if(session!=null){
			if(session.getAttribute("isValidUser") !=null){
			 if ((Boolean) session.getAttribute("isValidUser")) {
				 if((Boolean) session.getAttribute("isAdmin"))
					 return "redirect:"+AppConstants.URL_ADMIN_ORDERS;
				 else
					 return "redirect:"+AppConstants.URL_USER_HOME;
	        }
			}
		}
		return AppConstants.URL_OLDLOGIN;
	}
	
	@RequestMapping(value = {"/newaccount" } ,method = RequestMethod.GET)
	public String viewNewAccountPage(HttpServletRequest request, Model model) {
		HttpSession session = request.getSession(false);
		if(session!=null){
			User user = (User)session.getAttribute("user");
			if(user!=null){
				model.addAttribute("email", user.getEmail());
				return AppConstants.URL_NEW_ACCOUNT;
			} else {
				return "redirect:"+AppConstants.URL_OLDLOGIN;
			}
		} else {
			return "redirect:"+AppConstants.URL_OLDLOGIN;
		}
		
	}
	
	/**
	 * Method to handle the user authentication request.
	 * @param request
	 * @param response
	 * @param session
	 * @param model
	 * @param redir
	 * @return
	 */
	@RequestMapping(value = "/oldauthenticate", method = RequestMethod.POST)	
	public String authenticateUser(HttpServletRequest request, HttpServletResponse response, 
			HttpSession session, Model model, RedirectAttributes redir) {
		username = request.getParameter("username").trim();
		password = request.getParameter("password").trim();
		errors = new HashMap<String,String>();
		boolean isAdmin = false;
		// if fields are empty or invalid input return error
		if(!validateLoginForm()){
			if(!username_error.trim().isEmpty())redir.addFlashAttribute("username_error", username_error);
			if(!password_error.trim().isEmpty())redir.addFlashAttribute("password_error", password_error);
			if(errors.size()>0)redir.addFlashAttribute("errors", errors);
			return "redirect:"+AppConstants.URL_OLDLOGIN;
		}
		
		// Find the username from the database based on the user input
		// If we can retrieve the username, it means that the user exists !
		// What could happen ?
		String user_name = userOperation.retrieveByUsername(username);
		
		boolean isValidUser = false;
		if (user_name==null) {
			redir.addFlashAttribute("errorMessage", AppConstants.MSG_BAD_LOGIN_INPUT);
			return "redirect:"+AppConstants.URL_OLDLOGIN;
		}
		
		// validate the password
		isValidUser = userOperation.isValidUserPassword(user_name, password);
		
		// if password doesn't match return error
		if(!isValidUser){
			redir.addFlashAttribute("errorMessage", "Bad password");
			return "redirect:"+AppConstants.URL_OLDLOGIN;
		}
		
		User session_user = new User();
		session.setAttribute("user", session_user);
		session.setAttribute("isValidUser", false);
		session.setAttribute("isAdmin", false);

		// Exposed session tokens
		return "redirect:"+ AppConstants.URL_NEW_ACCOUNT;
	}
	
	/**
	 * Validate for Empty fields
	 * @return
	 */
	private boolean validateLoginForm(){
		boolean isValid=true;
		username_error="";
		password_error="";
		errors.clear();
		if(username==null || username.isEmpty()){
			isValid=false;
			username_error = AppConstants.MSG_REQUIRED_USERNAME;
		}
		if(password==null || password.isEmpty()){
			isValid=false;
			password_error = AppConstants.MSG_REQUIRED_PASSWORD;
		}
		if(isValid){
			boolean isValidInput = hasValidFormInputs();
			if(!isValidInput){
				logger.warn("Invalid string is used for login.");
				isValid=false;
				errors.put("invalidUserInput", AppConstants.MSG_BAD_LOGIN_INPUT);
			}
		}
		return isValid;
	}
	
	/**
	 * Validate for invalid value of text fields (xss attacks)
	 * @return
	 */
	private boolean hasValidFormInputs(){
		boolean isValidInput=true;

		// Fixme
		/*	try {
				if(username.contains("@") || username.indexOf("@")!=-1){
					isValidInput = ESAPI.validator().isValidInput("User Name", username, "Email", 30, false);
				}else{
					isValidInput = ESAPI.validator().isValidInput("User Name", username, "Username", 30, false);
				}
			}catch (IntrusionException e) {
				isValidInput=false;
			}*/
		return isValidInput;
	}
}