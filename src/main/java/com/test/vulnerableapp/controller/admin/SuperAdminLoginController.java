package com.test.vulnerableapp.controller.admin;

import com.test.vulnerableapp.security.SecurityUtils;
import com.test.vulnerableapp.util.AppConstants;
import org.h2.tools.Shell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Controller
public class SuperAdminLoginController {
	private static final Logger logger = LoggerFactory.getLogger(SuperAdminLoginController.class);

	@Autowired SecurityUtils securityUtils;
	
	private String JDBC_URL = "jdbc:h2:mem:testdb";
	
	@RequestMapping(value = "/admin/dbaccess")
	public ModelAndView viewDbAccessPage(HttpServletRequest request, HttpServletResponse response, 
			HttpSession session, Model model, RedirectAttributes redir) {
		ModelAndView mav = new ModelAndView(AppConstants.URL_ADMIN_DBACCES);
		
		if(request.getParameter("db_login") != null && request.getParameter("db_password") != null) {
			String db_login = request.getParameter("db_login");
			String db_password = request.getParameter("db_password");
			
			// Set cookies
			Cookie cookie_login = new Cookie("db_login", db_login);
			Cookie cookie_password = new Cookie("db_password", db_password);
			response.addCookie(cookie_login);
			response.addCookie(cookie_password);
			
			Connection conn = null;
			
			try {
				conn = DriverManager.getConnection(JDBC_URL, db_login, db_password);
			} catch (SQLException e) {
				mav.addObject("errorMessage", "Error while connecting to the database");
				mav.addObject("db_login", db_login);
				mav.addObject("db_password", db_password);
				return mav;
			}
			
			String successMessage = "Successful login... but work in progress - Go to temporary page to have limited access to database : ";
			
			successMessage += "<a href='http://127.0.0.1:8080/admin/tmpdbaccess?line=show tables;'>Temporary access</a>";
			mav.addObject("successMessage", successMessage);
			
		}
		
		return mav;
	}
	
	@RequestMapping(value = "/admin/tmpdbaccess")
	public ModelAndView viewTmpDbAccess(HttpServletRequest request, HttpServletResponse response, 
			HttpSession session, Model model, RedirectAttributes redir) throws IOException {
		ModelAndView mav = new ModelAndView(AppConstants.URL_ADMIN_DBACCES);
		
		Cookie[] cookies = request.getCookies();
		String db_login = null;
		String db_password = null;
		
		if(cookies != null) {
			for(Cookie cookie: cookies) {
				if (cookie.getName().equals("db_login")) {
					db_login = cookie.getValue();
				}
				if (cookie.getName().equals("db_password")) {
					db_password = cookie.getValue();
				}
			}
		}
		
		if(db_login != null & db_password != null && request.getParameter("line") != null) {
			String line = request.getParameter("line");
			
			Connection conn = null;
			
			try {
				conn = DriverManager.getConnection(JDBC_URL, db_login, db_password);
			} catch (SQLException e) {
				logger.warn("Failed");
				return mav;
			}
			
			try (PrintStream out = new PrintStream(response.getOutputStream())) {
		        out.println("\n> " + line);
		        try {
		            Shell shell = new Shell();
		            shell.setErr(out);
		            shell.setOut(out);
		            shell.runTool(conn, "-sql", line);
		        } catch (SQLException e) {
		            out.println(e.toString());
		        }
		    }
		}
		
		return mav;
	}

}
