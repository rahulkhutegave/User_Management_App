package com.user.main.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.user.main.exception.UserAppException;
import com.user.main.service.UserService;

@RestController
public class ForgotPwdRestController {
	@Autowired
	private UserService service;

	@GetMapping("/forgotPwd/{emailId}")
	public String forgotPwd(@PathVariable String emailId) throws UserAppException {

		boolean status = service.forgotPwd(emailId);

		if (status) {
			return "we have sent password to your email";
		} else {
			return "Please enter valid email id";
		}

	}

}