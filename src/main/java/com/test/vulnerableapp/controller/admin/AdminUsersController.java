package com.test.vulnerableapp.controller.admin;


import com.test.vulnerableapp.model.UserInfo;
import com.test.vulnerableapp.operations.UserOperation;
import com.test.vulnerableapp.util.AppConstants;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
@Controller
public class AdminUsersController {
	private static final Logger logger = Logger.getLogger(AdminUsersController.class);

	@Autowired
	private UserOperation userOperation;
	
	/**
	 * render the admin home page
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/admin/users", method = RequestMethod.GET)
	public String viewAdminHomePage(HttpServletRequest request, Model model) { 
		//logger.error("This is Error message at getLoginPage method ", new Exception("Page Error"));
		model.addAttribute("userList", initUserList(request));
		return AppConstants.URL_ADMIN_USERS;
	}
	

	 /*
     * Method used to populate the user list in view.
     * Note that here you can call external systems to provide real data.
     */
	public List<UserInfo> initUserList(HttpServletRequest request){
		UserInfo loginUser = (UserInfo)request.getSession(false).getAttribute("user");
		List<UserInfo> userList = null;
		userList = userOperation.retrieveAll();
		return userList;
	}	
}
