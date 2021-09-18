package com.user.main.service;

import java.io.IOException;
import java.util.Map;

import com.user.main.bindings.UserRegForm;
import com.user.main.exception.UserAppException;

public interface UserService {
	
public Map<Integer, String> getCountries();
	
	public Map<Integer, String> getStates(Integer countryId);
	
	public Map<Integer, String> getCities(Integer stateId);
	
	public String emailCheck(String emailId);
	
	public boolean saveUser(UserRegForm userForm) throws UserAppException, IOException;

}
