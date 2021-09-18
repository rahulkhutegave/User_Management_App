package com.user.main.restcontroller;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.user.main.bindings.UserRegForm;
import com.user.main.constants.AppConstants;
import com.user.main.exception.UserAppException;
import com.user.main.props.AppProperties;
import com.user.main.service.UserService;

@RestController
public class UserRegRestController {

	@Autowired
	private UserService userService;

	@Autowired
	private AppProperties appProps;

	@GetMapping("/countries")
	public Map<Integer, String> getCountries() {
		return userService.getCountries();
	}

	@GetMapping("/states/{countryId}")
	public Map<Integer, String> getStates(@PathVariable Integer countryId) {
		return userService.getStates(countryId);
	}

	@GetMapping("/cities/{stateId}")
	public Map<Integer, String> getCities(@PathVariable Integer stateId) {
		return userService.getCities(stateId);
	}

	@GetMapping("/emailcheck/{email}")
	public String uniqueEmailCheck(String email) {
		return userService.emailCheck(email);
	}

	@PostMapping("/saveUser")
	public String saveUser(@RequestBody UserRegForm userRegForm) throws UserAppException, IOException {
		boolean saveUser = userService.saveUser(userRegForm);

		if (saveUser) {
			return appProps.getMessages().get(AppConstants.USER_REG_SUCCESS);
		} else {
			return appProps.getMessages().get(AppConstants.USER_REG_FAIL);
		}
	}

}
