package com.test.vulnerableapp.controller;

import com.test.vulnerableapp.model.User;
import com.test.vulnerableapp.model.UserInfo;
import com.test.vulnerableapp.operations.UserOperation;
import com.test.vulnerableapp.security.SecurityUtils;
import com.test.vulnerableapp.util.AppConstants;
import com.test.vulnerableapp.util.AppUtil;
import com.test.vulnerableapp.util.ApplicationException;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.owasp.esapi.ESAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.security.auth.login.AccountLockedException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
@Controller
public class LoginController {
	private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

	@Autowired
	private UserOperation userOperation;
	@Autowired SecurityUtils securityUtils;

	private String username;
	private String password;
	private String username_error;
	private String password_error;	
	private Map<String,String> errors;
	 
	@ExceptionHandler(AccountLockedException.class)
	public ModelAndView redirectLocked(AccountLockedException ex) {
		return new ModelAndView("home")
			.addObject("errorMessage", AppConstants.MSG_ACCOUNT_LOCKED);
	
	}
	
	/**
	 * Method to render the login page
	 * @param request
	 * @return
	 */
	@RequestMapping(value = {"/login" } ,method = RequestMethod.GET)
	public String viewLoginPage(HttpServletRequest request, Model model) {
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
		model.addAttribute("user", new UserInfo());
		model.addAttribute("register", false);
		return AppConstants.URL_LOGIN;
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
	@RequestMapping(value = "/authenticate", method = RequestMethod.POST)	
	public String authenticateUser(HttpServletRequest request, HttpServletResponse response, 
			HttpSession session, Model model, RedirectAttributes redir) { 
		username = request.getParameter("username").trim();
		password = request.getParameter("password").trim();
		errors = new HashMap<String,String>();
		boolean isAdmin = false;
		String homePage = AppConstants.URL_USER_HOME;
		// if fields are empty or invalid input return error
		if(!validateLoginForm()){
			if(!username_error.trim().isEmpty())redir.addFlashAttribute("username_error", username_error);
			if(!password_error.trim().isEmpty())redir.addFlashAttribute("password_error", password_error);
			if(errors.size()>0)redir.addFlashAttribute("errors", errors);
			return "redirect:"+AppConstants.URL_LOGIN;
		}

		Optional<UserInfo> user = userOperation.retrieveByUsername(username);

		if (user.isPresent()) {

			boolean isValidUser = AppUtil.isValidPassword(password, user.get().getPassword());

			// if password doesn't match return error
			if (!isValidUser) {
				securityUtils.logLoginFailure(username);
				redir.addFlashAttribute("errorMessage", "Password doesn't match");
				return "redirect:/login";
			} else {
				securityUtils.checkAccountLocked(username);
			}

			User session_user = user.get();
			session.setAttribute("user", session_user);
			session.setAttribute("isValidUser", true);
			session.setAttribute("isAdmin", false);
			session.setAttribute("username", username);

			byte[] stream = null;
			try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
				 ObjectOutputStream oos = new ObjectOutputStream(baos)) {
				oos.writeObject(session_user.getRole_id());
				stream = baos.toByteArray();
				String data = Base64.getEncoder().encodeToString(stream);
				response.addCookie(new Cookie("SESSIONAPP", data));
			} catch (IOException e) {
				// Error in serialization
				e.printStackTrace();
			}

			// Exposed session tokens
			String _sessionID = session.getId();
			return "redirect:" + homePage + "?sid=" + _sessionID;
		} else {
			// Username Enumeration
			redir.addFlashAttribute("errorMessage", "User does'nt exists");
			return "redirect:" + AppConstants.URL_LOGIN;
		}

	}

	@RequestMapping(value = "/logout", method = RequestMethod.GET)	
	public String logoutUser(HttpServletRequest request, HttpServletResponse response, 
			Model model, RedirectAttributes redir) {
		HttpSession session = request.getSession();
		response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
        response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
        response.setDateHeader("Expires", 0);
		if(session!=null){
			session.removeAttribute("user");
			session.removeAttribute("isValidUser");
			session.removeAttribute("username");
			session.removeAttribute("isAdmin");
		}
		session.invalidate();
		
		// Unvalidated Redirects and Forwards
		if (!StringUtils.isEmpty(request.getParameter("redirect"))) {
			return "redirect:"+request.getParameter("redirect");
		}
		
		redir.addFlashAttribute("successMessage", AppConstants.MSG_LOGOUT_SUCCESS);
		return "redirect:"+AppConstants.URL_LOGIN;
	}
	
	@RequestMapping(value = "/invalidUser", method = RequestMethod.GET)	
	public String logoutInvalidUser(HttpServletRequest request, RedirectAttributes redir) {
		redir.addFlashAttribute("errorMessage", AppConstants.MSG_INVALID_USER);
		HttpSession session = request.getSession();
		if(session!=null){
			session.removeAttribute("user");
			session.removeAttribute("isValidUser");
			session.removeAttribute("username");
			session.removeAttribute("isAdmin");
		}
		session.invalidate();
		return "redirect:"+AppConstants.URL_LOGIN;
	}
	
	@RequestMapping(value = "/error", method = RequestMethod.GET)	
	public String errorPage(HttpServletRequest request) {
		return AppConstants.URL_ERROR;
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
		boolean isValidInput;
		try {
			if(username.contains("@") || username.indexOf("@")!=-1){
				isValidInput = ESAPI.validator().isValidInput("User Name", username, "Email", 30, false);
			}else{
				isValidInput = ESAPI.validator().isValidInput("User Name", username, "Username", 30, false);
			}
		} catch (ApplicationException e) {
			isValidInput=false;
		}
		return isValidInput;
	}
}