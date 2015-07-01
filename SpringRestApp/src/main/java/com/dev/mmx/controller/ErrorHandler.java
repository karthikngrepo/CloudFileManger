package com.dev.mmx.controller;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 
 * @author Karthik N G
 *
 */
@RestController
@RequestMapping(value= "/error")
public class ErrorHandler {
	
	private Logger log = Logger.getLogger(ErrorHandler.class);
	
	@RequestMapping(value="/error", method=RequestMethod.POST)
	public void handleError() throws Exception {
		log.error("Encounter error on the program execution");
	}
}