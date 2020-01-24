package com.test.vulnerableapp.controller.admin;

import com.test.vulnerableapp.model.UserInfo;
import com.test.vulnerableapp.operations.UserOperation;
import com.test.vulnerableapp.security.SecurityUtils;
import com.test.vulnerableapp.util.AppConstants;
import com.test.vulnerableapp.util.AppUtil;
import org.owasp.esapi.errors.IntrusionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class AdminLoginController {
	private static final Logger logger = LoggerFactory.getLogger(AdminLoginController.class);

	@Autowired
	private UserOperation userOperation;
	@Autowired SecurityUtils securityUtils;

	private String key;
	private String key_error;
	private Map<String,String> errors;
	
	@RequestMapping(value = "/admin" ,method = RequestMethod.GET)
	public ModelAndView viewLoginPage() {
		//logs debug message
		if(logger.isDebugEnabled()){
			logger.debug("Admin Controller is executed!");
		}
		//logs exception
		//logger.error("This is Error message at getLoginPage method ", new Exception("Page Error"));
		ModelAndView model = new ModelAndView(AppConstants.URL_ADMIN_LOGIN);
		return model;
	}
	
	@RequestMapping(value = "admin/authenticate", method = RequestMethod.POST)	
	public String authenticateUser(HttpServletRequest request, HttpServletResponse response, 
			HttpSession session, Model model, RedirectAttributes redir) { 
		key = request.getParameter("key").trim();
		errors = new HashMap<String,String>();
		
		// if fields are empty or invalid input return error
		if(!validateLoginForm()){
			if(!key_error.trim().isEmpty())redir.addFlashAttribute("key_error", key_error);
			if(errors.size()>0)redir.addFlashAttribute("errors", errors);
			return "redirect:/admin";
		}
		
		UserInfo user = (UserInfo)request.getSession(false).getAttribute("user");
		
		if(user==null){
			logger.warn("User not logged in");
			redir.addFlashAttribute("errorMessage", AppConstants.MSG_BAD_LOGIN_INPUT);
			return "redirect:/login";
		}
		if(user.getRole_id()!=AppConstants.ADMIN_ROLE_ID){
			logger.warn("User is not an admin");
			redir.addFlashAttribute("errorMessage", AppConstants.MSG_BAD_LOGIN_INPUT);
			return "redirect:/admin";
		}
		boolean isValidKey = AppUtil.isValidKey(key, user.getSuperkey());
		if(!isValidKey){
			logger.info("Admin key not valid");
			redir.addFlashAttribute("errorMessage", AppConstants.MSG_BAD_LOGIN_INPUT);
			return "redirect:/admin";
		}

		session.setAttribute("isAdmin", true);
		session.setAttribute("role", AppConstants.ADMIN_ROLE_ID);
		//logs exception
		//logger.error("This is Error message at getLoginPage method ", new Exception("Page Error"));
		return "redirect:/admin/orders";
	}

	@RequestMapping(value = "admin/logout", method = RequestMethod.GET)	
	public String logoutUser(HttpServletRequest request, HttpServletResponse response, 
									RedirectAttributes redir) {
		HttpSession session = request.getSession();
		if(session!=null){
			session.removeAttribute("user");
			session.removeAttribute("isValidUser");
			session.removeAttribute("username");
			session.removeAttribute("role");
		}
		session.invalidate();
		redir.addFlashAttribute("successMessage", AppConstants.MSG_LOGOUT_SUCCESS);
		return "redirect:/admin";
	}
	
	@RequestMapping(value = "/invalidAdmin", method = RequestMethod.GET)	
	public String logoutInvalidUser(HttpServletRequest request, RedirectAttributes redir) {
		redir.addFlashAttribute("errorMessage", AppConstants.MSG_INVALID_USER);
		return "redirect:/admin";
	}
	
	/**
	 * Validate for Empty fields
	 * @return
	 */
	private boolean validateLoginForm(){
		boolean isValid=true;
		key_error="";
		errors.clear();
		if(key==null || key.isEmpty()){
			isValid=false;
			key_error = AppConstants.MSG_REQUIRED_USERNAME;
		}
		if(isValid){
			boolean isValidInput = hasValidFormInputs();
			if(!isValidInput){
				logger.warn("Invalid string is used for login.");
				isValid=false;
				errors.put("invalidUserInput", AppConstants.MSG_BAD_LOGIN_INPUT);
			}
		}
		//String safeOutput = ESAPI.encoder().encodeForHTML( validInput );
		return isValid;
	}
	
	/**
	 * Validate for invalid value of text fields (xss attacks)
	 * @return
	 */
	private boolean hasValidFormInputs(){
		boolean isValidInput=true;
		String hasValidInput;
		// TODO : Validate key
			try {
				/*if(username.contains("@") || username.indexOf("@")!=-1){
					hasValidInput = ESAPI.validator().getValidInput("User Name", username, "Email", 30, false);
				}else{
					hasValidInput = ESAPI.validator().getValidInput("User Name", username, "Username", 30, false);
				}*/
				System.out.println("Try");
			//} catch (ValidationException e) {
			//	isValidInput=false;
				//logger.warn("Invalid string is used for login.", new Exception("Bad Credentials."));
				//e.printStackTrace();
			} catch (IntrusionException e) {
				isValidInput=false;
				//e.printStackTrace();
			}
		return isValidInput;
	}
	
	 /*
     * Method used to populate the user list in view.
     * Note that here you can call external systems to provide real data.
     */
	@ModelAttribute("userList")
	public List<UserInfo> initUserList(){
		List<UserInfo> userList = userOperation.retrieveAll();
		return userList;
	}
}
