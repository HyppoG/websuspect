package com.test.vulnerableapp.controller.admin;

import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.owasp.esapi.ESAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.test.vulnerableapp.service.AdminService;
import com.test.vulnerableapp.util.AppConstants;
import com.test.vulnerableapp.util.AppUtil;

@Controller
public class AdminSettingsController {
	private static final Logger logger = Logger.getLogger(AdminSettingsController.class);
	@Value("${upload.maxsize}")
	private String UPLOAD_MAX_SIZE;
	@Value("${include.file_extension}")
	private String INCLUDE_FILE_EXTENSION;
	@Value("${upload.location}")
	private String UPLOAD_DIR_PATH;
	@Value("${shippingXML}")
	private String SHIPPING_XML;

	@Autowired
	private AdminService adminService;
	
	/**
	 * render the File Upload Page
	 * @return
	 */
	@RequestMapping(value = {"/admin/settings","/admin/settings/upload" } ,method = RequestMethod.GET)
	public String viewUploadPage() {
		//logs debug message
		if(logger.isDebugEnabled()){
			logger.debug("Admin Settings Controller is executed!");
		}
		return AppConstants.URL_ADMIN_SETTINGS;
	}
	
	 /**
     * Upload file
     */
    @RequestMapping(value = "/admin/settings/upload", method = RequestMethod.POST)
    public String uploadFileHandler(@RequestParam("fileName") MultipartFile fileName, Model model, RedirectAttributes redir, 
            HttpServletRequest request) {
    	
    	boolean isValidFormFields=true;
    	
		/*try {
			isValidFormFields = validateForm(userFileInfo,model); // field validation
		} catch (ValidationException e1) {
			isValidFormFields=false;
			e1.printStackTrace();
		}
		if(!isValidFormFields){
			model.addAttribute("errorMessage", AppConstants.MSG_FORM_CONTAINS_EROOR);
			return AppConstants.URL_ADMIN_SETTINGS;
		}*/

    	if (!fileName.isEmpty()) {
           try {
        	    if(UPLOAD_MAX_SIZE.isEmpty())UPLOAD_MAX_SIZE="0";
	           	String uploadFileName = fileName.getOriginalFilename();
        	   
	           	// check for invalid file if size is more then specified in property file
	           	if(!AppUtil.isValidFileSize(fileName.getSize(),Long.valueOf(UPLOAD_MAX_SIZE.trim()))){
        		   model.addAttribute("errorMessage", "File is not valid.Can not upload more then "+Long.valueOf(UPLOAD_MAX_SIZE.trim())/(1024*1024)+" MB file!");
        		   return AppConstants.URL_ADMIN_SETTINGS;
	        	   }
           	
        	   	// check for invalid extensions, if specified in property file
	           	if(!AppUtil.isValidFileExtenstion(uploadFileName, INCLUDE_FILE_EXTENSION)){
	           		model.addAttribute("errorMessage", "File is invalid.File must have a .xml extension");
	           		return AppConstants.URL_ADMIN_SETTINGS;
	           	}
	           	
	           	// check for invalid filename
	           	if(!fileName.getOriginalFilename().equals(SHIPPING_XML)){
	           		model.addAttribute("errorMessage", "File is invalid.The filename must be " + SHIPPING_XML);
	           		return AppConstants.URL_ADMIN_SETTINGS;
	           	}
            	
            	URL url = this.getClass().getClassLoader().getResource(SHIPPING_XML);
            	Path uploadDirPath = Paths.get(url.toURI().resolve(".").getPath());
            	
            	 // Create the file on server
                AppUtil.store(uploadDirPath, fileName);
            	
                redir.addFlashAttribute("successMessage", "You successfully uploaded file " + ESAPI.encoder().encodeForHTML(uploadFileName));
                return "redirect:/admin/settings";
            } catch (Exception e) {
            	redir.addFlashAttribute("errorMessage", "An internal error occured, failed to upload file.");
            	e.printStackTrace();
            	return "redirect:/admin/settings";
            }
        } else {
        	model.addAttribute("errorMessage", "Failed to upload because the file was empty.");
        	return AppConstants.URL_ADMIN_SETTINGS;
        }
    }
    
    @RequestMapping(value = "/admin/systemInfo", method = RequestMethod.GET)
	public String systemInfo(Model model, RedirectAttributes redirectAttributes) {
		String response = adminService.getSystemInfo();
		if (response.equals("Operation Failed")) {
			redirectAttributes.addFlashAttribute("message", response);
			return "redirect:/home";
		}
		logger.debug("Getting system info");
		model.addAttribute("systemInfo", response);
		return "/admin/systemInfo";
	}
}