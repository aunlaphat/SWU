package com.arg.swu.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.arg.swu.commons.ApiConstant;

/**
 * 
 * @author sitthichaim
 *
 */
@RestController
@RequestMapping("login")
public class LoginCtrl implements ApiConstant {
	
	private static final Logger LOG = LogManager.getLogger(LoginCtrl.class);

	@GetMapping("hello")
	public String hello() {
		return "hello !";
	}
}
