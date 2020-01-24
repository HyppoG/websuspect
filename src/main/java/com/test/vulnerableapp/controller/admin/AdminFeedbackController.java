package com.test.vulnerableapp.controller.admin;

import com.test.vulnerableapp.model.Feedback;
import com.test.vulnerableapp.operations.FeedbackOperation;
import com.test.vulnerableapp.util.AppConstants;
import com.test.vulnerableapp.util.AppUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;

@Controller
public class AdminFeedbackController {
	private static final Logger logger = Logger.getLogger(AdminFeedbackController.class);
	@Autowired
	private FeedbackOperation feedbackOperation;
	
	/**
	 * Method is used to render the Admin Feedback Report page
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/admin/feedbacks", method = RequestMethod.GET)	
	public String viewFeedbackReport(Model model) { 
		logger.info("This is feedback report controller.");
		List<Feedback> feedbacksList = initFeedbacksList();
		if(feedbacksList.size()==0)model.addAttribute("successMessage", "There is no Feedback from any user.");
		model.addAttribute("feedbacksList", feedbacksList);
		return AppConstants.URL_ADMIN_FEEDBAKS;
	}
	

	/**
     * Method used to populate the feedbacks list in view.
     */
	public List<Feedback> initFeedbacksList(){
		List<Feedback> feedbackList = feedbackOperation.retrieveAll();
		// Stored XSS
		//if(feedbacksList.size()>0)feedbacksList = (List<Feedback>) senitizeList(feedbacksList);
		return feedbackList;
	}
	
	// method will sanitize the data coming from database
	private List<Feedback> senitizeList(List<Feedback> feedbacksList){
		List<Feedback> list = new ArrayList<Feedback>();
		for(Feedback feedback : feedbacksList){
			feedback.setUsername(AppUtil.encodeForHTML(feedback.getUsername()));
			feedback.setEmail(AppUtil.encodeForHTML(feedback.getEmail()));
			feedback.setFeedback(AppUtil.encodeForHTML(feedback.getFeedback()));
			list.add(feedback);
		}
		return list;
	}
}