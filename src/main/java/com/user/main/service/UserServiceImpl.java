package com.user.main.service;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.user.main.bindings.UserRegForm;
import com.user.main.exception.UserAppException;
import com.user.main.props.AppProperties;
import com.user.main.repository.CitiesMasterRepository;
import com.user.main.repository.CountryMasterRepository;
import com.user.main.repository.StatesMasterRepository;
import com.user.main.repository.UserAccountsRepository;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private CountryMasterRepository countryRepo;

	@Autowired
	private StatesMasterRepository stateRepo;

	@Autowired
	private CitiesMasterRepository cityRepo;

	@Autowired
	private UserAccountsRepository userRepo;

	@Autowired
	private AppProperties appProps;

	@Override
	public Map<Integer, String> getCountries() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<Integer, String> getStates(Integer countryId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<Integer, String> getCities(Integer stateId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String emailCheck(String emailId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean saveUser(UserRegForm userForm) throws UserAppException, IOException {
		// TODO Auto-generated method stub
		return false;
	}
	
	
	
	
	
	
	
	
	
	
	
}
