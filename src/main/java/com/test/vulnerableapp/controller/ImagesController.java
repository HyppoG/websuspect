/**
 * 
 */
package com.test.vulnerableapp.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.NoSuchFileException;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;


/**
 * Provide protected access to image flights and other internal media assets.
 * 
 */
@Controller
public class ImagesController {

	@Value("${flight-images}")
	private String FLIGHT_IMAGE_DIR;
	
	@PostConstruct
	public void setup() {
		File flightImageDir = new File(FLIGHT_IMAGE_DIR);
		if (!flightImageDir.exists()) {
			flightImageDir.mkdirs();
		}
	}
	
	@ResponseStatus(value=HttpStatus.NOT_FOUND)
	@ExceptionHandler(NoSuchFileException.class)
	public void noSuchFileException(Exception e) {
		e.printStackTrace();
	}
	
	/**
	 * Fetch the image and display if available.
	 * 
	 * @param imgPath
	 * @param request
	 * @param response
	 * @throws NoSuchFileException
	 */
	@RequestMapping(value="/images")
	public void fileRequest(@RequestParam("file") String imgPath, HttpServletRequest request, HttpServletResponse response) 
			throws IOException {
		
		File imageDir = new File(FLIGHT_IMAGE_DIR);
		File file = new File(FLIGHT_IMAGE_DIR + File.separator + imgPath);
		
		if (!file.exists()) {
			throw new NoSuchFileException(file.getAbsolutePath());
		}
		
		byte[] b = new byte[256];
		try (OutputStream os = response.getOutputStream();
			FileInputStream fis = new FileInputStream(file)) {
			while (fis.read(b) > -1) {
				os.write(b);
			}
		}
		
	}
	
}