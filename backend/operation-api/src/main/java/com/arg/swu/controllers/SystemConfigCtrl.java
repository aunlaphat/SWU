package com.arg.swu.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.arg.swu.commons.ApiConstant;

/**
 * 
 * @author sitthichaim
 *
 */
@RestController
@RequestMapping("system-config")
public class SystemConfigCtrl implements ApiConstant {
	
	private static final Logger LOG = LogManager.getLogger(SystemConfigCtrl.class);


}
