package com.test.vulnerableapp.controller;

import com.test.vulnerableapp.operations.UserOperation;
import com.test.vulnerableapp.util.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Pattern;

/**
 * 
 * Allow an authenticated user to manage its profile.
 * 
 *
 */

@Controller
@RequestMapping("/passwordRecovery")
public class PasswordRecoveryController {

	@Autowired
	private UserOperation userOperation;
	
	Pattern emailRegex = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
	
	
	@RequestMapping(method=RequestMethod.GET)
	public String showForm(HttpServletRequest request, HttpServletResponse response) {
		return AppConstants.URL_PASSWORD_RECOVERY;
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public String recover(@RequestParam(value="email") String email, RedirectAttributes redir) throws IOException {
		
		if (!emailRegex.matcher(email).matches()) {
			redir.addFlashAttribute("errorMessage", email+" is not a valid email address.");
			return "redirect:"+AppConstants.URL_PASSWORD_RECOVERY;
		}

		if (!userOperation.isUserExistsByEmail(email)){
			redir.addFlashAttribute("errorMessage", "No user registered with this e-mail in the database");
			return "redirect:"+AppConstants.URL_PASSWORD_RECOVERY;
		}
		
		redir.addFlashAttribute("successMessage", "You should receive an e-mail with your password");
		return "redirect:"+AppConstants.URL_PASSWORD_RECOVERY;
	}
	
}