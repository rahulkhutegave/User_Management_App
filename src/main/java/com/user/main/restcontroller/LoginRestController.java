package com.user.main.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.user.main.bindings.LoginForm;
import com.user.main.exception.UserAppException;
import com.user.main.service.UserService;

@RestController
public class LoginRestController {

	@Autowired
	private UserService service;

	@PostMapping("/login")
	public String login(@RequestBody LoginForm loginFrom) throws UserAppException {

		return service.loginCheck(loginFrom);
	}

}
