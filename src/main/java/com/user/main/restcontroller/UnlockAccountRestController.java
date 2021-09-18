package com.user.main.restcontroller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.user.main.bindings.UnlockAccountForm;
import com.user.main.exception.UserAppException;
import com.user.main.service.UserService;

@RestController
public class UnlockAccountRestController {

	@Autowired
	private UserService service;

	@PostMapping("/unlock")
	public String unlockUserAccount(@RequestBody UnlockAccountForm unlockAccForm) throws UserAppException {
		boolean status = service.unlockAccount(unlockAccForm);
		if (status) {
			return "Account unlocked Successfully";
		} else {
			return "Incorrect Temporary Pazzword";
		}
	}

}
