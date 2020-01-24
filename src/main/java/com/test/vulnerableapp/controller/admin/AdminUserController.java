package com.test.vulnerableapp.controller.admin;

import com.test.vulnerableapp.model.Role;
import com.test.vulnerableapp.model.UserInfo;
import com.test.vulnerableapp.operations.UserOperation;
import com.test.vulnerableapp.service.RoleService;
import com.test.vulnerableapp.util.AppConstants;
import com.test.vulnerableapp.util.AppUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.ValidationException;
import java.util.List;
import java.util.Optional;
@Controller
public class AdminUserController{
	private static final Logger logger = LoggerFactory.getLogger(AdminUserController.class);
	@Autowired
	private UserOperation userOperation;
	@Autowired
	private RoleService roleService;
	//private Validator validator=new UserInfoValidator();
	
	/**
	 * Mehod is used to render the user registration form
	 * @param model
	 * @return
	 */
	@RequestMapping(value = {"admin/user","admin/addUser","admin/addAdmin", "admin/editUser"}, method = RequestMethod.GET)
	public String newUserForm(HttpServletRequest request, Model model, RedirectAttributes redir) {
		//logger.error("This is Error message at getLoginPage method ", new Exception("Page Error"));
		String path = request.getServletPath();
		Boolean isAdmin = false;
		UserInfo user = new UserInfo();

		if (path.equals("/admin/addAdmin")) {
			isAdmin = true;
			user.setRole_id(1001);
		} else {
			user.setRole_id(1002);
		}

		model.addAttribute("user", user);
		model.addAttribute("isAdmin", isAdmin);
		return AppConstants.URL_ADMIN_USER;
	}
	
	/**
	 * Method used to save user information in database
	 * @param user
	 * @param result
	 * @param model
	 * @return
	 */
	@RequestMapping(value = {"admin/addUser", "admin/addAdmin"}, method = RequestMethod.POST)
	public String AddUser(@ModelAttribute(value="user") @Validated UserInfo user, BindingResult result, 
			Model model,HttpServletRequest request, RedirectAttributes redir) {
		// validate user information
		this.validate(user, result);
		
		if(result.hasErrors()){
			model.addAttribute("errorMessage", AppConstants.MSG_FORM_CONTAINS_EROOR);
			return AppConstants.URL_ADMIN_USER;
		}	
		
		// check user already exist or not
		if(userOperation.isUserExistsByUsername(user.getUsername())){
			//redir.addFlashAttribute("errorMessage", "The username '"+user.getusername()+"' already registered.");
			model.addAttribute("errorMessage", "The username '"+user.getUsername()+"' is not valid or already registered.");
			return AppConstants.URL_ADMIN_USER;
		}
		
		if(user.getRole_id() == AppConstants.ADMIN_ROLE_ID) {
			String key = AppUtil.getEncryptedKey(user.getSuperkey());
			user.setSuperkey(key);
		}
		
		String password = AppUtil.getEncryptedPassword(user.getPassword());
		user.setPassword(password);		
		user.setStatus("ACTIVE");
		// save user information to database

		userOperation.addUser(user);
		redir.addFlashAttribute("successMessage", AppConstants.MSG_USER_SAVED);
		model.addAttribute("isEdit", true);
		//logger.error("This is Error message at getLoginPage method ", new Exception("Page Error"));
		return "redirect:/admin/home";
	}
	
	
	/**
	 * Method used to open the edit user form
	 * @param user
	 * @param result
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/admin/editUser/{id}", method = RequestMethod.GET)	
	public String editUserForm(@PathVariable("id") long id, HttpServletRequest request, Model model,RedirectAttributes redir) {
		// validate user information
		/*if(id==null || id.isEmpty() || id.equals(-1)){
			//model.addAttribute("errorMessage", AppConstants.MSG_SELECT_DROPDOWN_USER);
			//return AppConstants.URL_ADMIN_HOME;
			redir.addFlashAttribute("errorMessage", AppConstants.MSG_SELECT_DROPDOWN_USER);
			return "redirect:/admin/home";
		}*/
		Optional<UserInfo> user = userOperation.retrieveById(id);

		if(!user.isPresent()){
			redir.addFlashAttribute("errorMessage", AppConstants.MSG_USER_NOT_EXIST);
			return "redirect:/admin/home";
		}
		Boolean isAdmin = false;
		if(user.get().getRole_id() == 1001) {
			isAdmin = true;
		}
		user.get().setPassword("");
		model.addAttribute("isEdit", true);
		model.addAttribute("user", user.get());
		model.addAttribute("isAdmin", isAdmin);
		return AppConstants.URL_ADMIN_USER;
	}
	
	/**
	 * Method used to update user information in database
	 * @param user
	 * @param result
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "admin/updateUser", method = RequestMethod.POST)
	public String updateUser(@ModelAttribute(value="user") @Validated UserInfo user, BindingResult result, 
			Model model, HttpServletRequest request, RedirectAttributes redir) {
		// validate user information
		//UserInfoValidator validatorEdit = (UserInfoValidator)validator;
		//validatorEdit.validate(user, result, true);
		
		if(result.hasErrors()){
			model.addAttribute("isEdit", true);
			model.addAttribute("errorMessage", AppConstants.MSG_FORM_CONTAINS_EROOR);
			return AppConstants.URL_ADMIN_USER;
		}
		
		//  get the user for db
		UserInfo dbuser = userOperation.retrieveById(user.getId()).get();
		
		// check if anyone try to change site admin role to customer role
		if(dbuser.getIsSiteAdmin()==AppConstants.YES && user.getRole_id()!=AppConstants.ADMIN_ROLE_ID){
			model.addAttribute("isEdit", true);
			model.addAttribute("errorMessage", "Can not change site admin to customer.");
			return AppConstants.URL_ADMIN_USER;
		}
		
		// check if user has changed username
		if(!user.getUsername().equalsIgnoreCase(dbuser.getUsername())){
			// check user already exist or not
			if(userOperation.isUserExistsByUsername(user.getUsername())){
				model.addAttribute("isEdit", true);
				model.addAttribute("errorMessage", "The username '"+user.getUsername()+"' is not valid or already registered.");
				return AppConstants.URL_ADMIN_USER;
			}
		}
		
		// check if user changed the password or not. If field is empty then no need to update the password
		if(user.getPassword()!=null && !user.getPassword().isEmpty()){
			String password = AppUtil.getEncryptedPassword(user.getPassword());
			user.setPassword(password);
		}else{
			user.setPassword(dbuser.getPassword());
		}
		
		// check if user changed the password or not. If field is empty then no need to update the password
		if(user.getSuperkey()!=null && !user.getSuperkey().isEmpty()){
			String key = AppUtil.getEncryptedKey(user.getSuperkey());
			user.setSuperkey(key);
		}else{
			user.setSuperkey(dbuser.getSuperkey());
		}
		
		user.setStatus(dbuser.getStatus());
		user.setIsSiteAdmin(dbuser.getIsSiteAdmin());
		// update the user
		userOperation.updateUser(user);
		redir.addFlashAttribute("successMessage", AppConstants.MSG_USER_UPDATED);
		return "redirect:/admin/home";
	}
	
	/**
	 *  Delete the user from database.	
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "admin/deleteUser/{id}", method = RequestMethod.GET)
	public String deleteUser(@PathVariable("id") long id, HttpServletRequest request, Model model, RedirectAttributes redir) {
		// validate user information
		/*String idString = request.getParameter("username");
		if(idString==null || idString.isEmpty() || idString.equals("-1")){
			redir.addFlashAttribute("errorMessage",  AppConstants.MSG_SELECT_DROPDOWN_USER);
			redir.addFlashAttribute("username_error", "Please select the username.");
			return "redirect:/admin/home";
		}
		long id = Long.valueOf(idString);*/
		
		// check selected user is not logged in
		HttpSession session = request.getSession(false);
		UserInfo user = (UserInfo)session.getAttribute("user");
		if(user==null){
			return "redirect:/invalidAdmin";
		}
		Optional<UserInfo> dbuser = userOperation.retrieveById(id);
		if(!dbuser.isPresent()){
			redir.addFlashAttribute("errorMessage", AppConstants.MSG_USER_NOT_EXIST);
			return "redirect:/admin/home";
		}
		
		if(dbuser.get().getIsSiteAdmin()==AppConstants.YES){
			redir.addFlashAttribute("errorMessage","Can not delete site admin.");
			return "redirect:/admin/home";
		}
		if(user.getId()==id){
			redir.addFlashAttribute("errorMessage","User is Logged in, can not delete itself.");
			return "redirect:/admin/home";
		}

		userOperation.deleteUserById(id);
		redir.addFlashAttribute("successMessage", AppConstants.MSG_USER_DELETED);
		return "redirect:/admin/home";
	}

	/**
     * Method used to populate the user role list in view.
    */
	@ModelAttribute("userRolesList")
	public List<Role> initUserRolesList(){
		List<Role> userRoles = roleService.getAllRoleList();
		return userRoles;
	}
	
	
	/**
    * Method used to populate the user list in view.
    */
	@ModelAttribute("userList")
	public List<UserInfo> initUserList(){
		List<UserInfo> userList = userOperation.retrieveAll();
		return userList;
	}
	
	public void validate(Object target, Errors errors) {
		UserInfo userBean = (UserInfo)target;

		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "username.required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "email.required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "password.required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password2", "password2.required");
		
		//if(userBean.getPassword().length()<8) errors.rejectValue("password","password.length");
		try {
			if(!AppUtil.isValidInput("Password", userBean.getPassword(), "Password", 30, false)){
				errors.rejectValue("password","password.length");
				errors.rejectValue("password","password.digit");
				errors.rejectValue("password","password.lowercase");
				errors.rejectValue("password","password.uppercase");
				errors.rejectValue("password","password.specialchar");
				errors.rejectValue("password","password.nowhitespace");
			}
			
			if(!AppUtil.isValidInput("Username", userBean.getUsername(), "Username", 30, false)){
				errors.rejectValue("username","name.invalid");
			}
			
			if(!AppUtil.isValidInput("Email", userBean.getEmail(), "Email", 70, false)){
                errors.rejectValue("email","email.invalid");
			}
			
			if(!AppUtil.isValidInput("SuperKey", userBean.getSuperkey(), "SuperKey", 4, false)){
                errors.rejectValue("superkey","superkey.invalid");
			}

		} catch (ValidationException e) {
			e.printStackTrace();
		}
		
       if(!userBean.getPassword().equals(userBean.getPassword2()))
    	   errors.rejectValue("password2","password2.notmatch");
	}
}
