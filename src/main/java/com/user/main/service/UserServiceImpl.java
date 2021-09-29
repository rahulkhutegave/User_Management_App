package com.user.main.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import com.user.main.bindings.LoginForm;
import com.user.main.bindings.UnlockAccountForm;
import com.user.main.bindings.UserRegForm;
import com.user.main.constants.AppConstants;
import com.user.main.entity.CitiesMasterEntity;
import com.user.main.entity.CountryMasterEntity;
import com.user.main.entity.StateMasterEntity;
import com.user.main.entity.UserAccountEntity;
import com.user.main.exception.FileMissingException;
import com.user.main.exception.PasswordEncryptDecryptException;
import com.user.main.exception.UserAppException;
import com.user.main.props.AppProperties;
import com.user.main.repository.CitiesMasterRepository;
import com.user.main.repository.CountryMasterRepository;
import com.user.main.repository.StatesMasterRepository;
import com.user.main.repository.UserAccountsRepository;
import com.user.main.utils.EmailUtils;
import com.user.main.utils.PwdUtils;

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
	private EmailUtils emailUtils;

	@Autowired
	private AppProperties appProps;

	@Override
	public Map<Integer, String> getCountries() {
		Map<Integer, String> countries = new HashMap<Integer, String>();
		List<CountryMasterEntity> allCountries = countryRepo.findAll();
		allCountries.forEach(country -> countries.put(country.getCountryId(), country.getCountryName()));
//		return allCountries.stream().collect(Collectors.toMap(CountryMasterEntity::getCountryId, CountryMasterEntity::getCountryName));
		return countries;
	}

	@Override
	public Map<Integer, String> getStates(Integer countryId) {
		Map<Integer, String> statesMap = new HashMap<Integer, String>();
		List<StateMasterEntity> states = stateRepo.findByCountryId(countryId);
		states.forEach(state -> statesMap.put(state.getStateId(), state.getStateName()));
		
//		return states.stream().collect(Collectors.toMap(StateMasterEntity::getStateId, StateMasterEntity::getStateName));
		
		return statesMap;
	}

	@Override
	public Map<Integer, String> getCities(Integer stateId) {
		Map<Integer, String> citiesMap = new HashMap<Integer, String>();
		List<CitiesMasterEntity> cities = cityRepo.findByStateId(stateId);
		cities.forEach(city -> citiesMap.put(city.getCityid(), city.getCityName()));
//		return cities.stream().collect(Collectors.toMap(CitiesMasterEntity::getCityid, CitiesMasterEntity::getCityName));
		
		return citiesMap;
	}

	@Override
	public String emailCheck(String emailId) {

		UserAccountEntity entity = new UserAccountEntity();
		entity.setEmail(emailId.trim());

		Example<UserAccountEntity> example = Example.of(entity);

		Optional<UserAccountEntity> findOne = userRepo.findOne(example);

		if (findOne.isPresent()) {
			return AppConstants.DUPLICATE;
		} else {
			return AppConstants.UNIQUE;
		}

	}

	@Override
	public boolean saveUser(UserRegForm userForm) throws UserAppException, IOException {

		UserAccountEntity entity = new UserAccountEntity();

		BeanUtils.copyProperties(userForm, entity);
		entity.setAccStatus(AppConstants.ACC_LOCKED);
		String randomPwd = generateRandomPazzwrd(6);
		String encryptedPwd;

		encryptedPwd = PwdUtils.encryptMsg(randomPwd);
		entity.setPazzword(encryptedPwd);

		entity = userRepo.save(entity);
		String emailBody = readUnlockAccEmailBody(entity);
		String subject = appProps.getMessages().get(AppConstants.UNLOCK_ACC_EMAIL_SUB);
		boolean status = emailUtils.sendEmail(userForm.getEmail(), subject, emailBody);

		return entity.getUserId() != null ? true : false;
	}

	private static String generateRandomPazzwrd(int length) {
		StringBuilder sb = new StringBuilder();
		Random random = new Random();
		for (int i = 0; i < length; i++) {
			sb.append(AppConstants.CANDIDATE_CHARS.charAt(random.nextInt(AppConstants.CANDIDATE_CHARS.length())));
		}

		return sb.toString();
	}

	private String readUnlockAccEmailBody(UserAccountEntity entity)
			throws IOException, PasswordEncryptDecryptException {
		StringBuilder sb = new StringBuilder(AppConstants.EMPTY_STR);
		String mailBody = AppConstants.EMPTY_STR;

		String fileName = appProps.getMessages().get(AppConstants.UNLOCK_ACC_EMAIL_BODY_FILE);
		FileReader fr = new FileReader(fileName);

		BufferedReader br = new BufferedReader(fr);

		String line = br.readLine();
		while (line != null) {
			sb.append(line);
			line = br.readLine();
		}
		fr.close();
		br.close();

		String decryptedPwd = PwdUtils.decryptMsg(entity.getPazzword());

		mailBody = sb.toString();

		mailBody = mailBody.replace(AppConstants.FNAME, entity.getFname());
		mailBody = mailBody.replace(AppConstants.LNAME, entity.getLname());
		mailBody = mailBody.replace(AppConstants.TEMP_PWD, decryptedPwd);
		mailBody = mailBody.replace(AppConstants.EMAIL, entity.getEmail());

		return mailBody;

	}

	@Override
	public boolean unlockAccount(UnlockAccountForm unlockAccForm) throws UserAppException {
		String email = unlockAccForm.getEmail();
		String tempPwd = unlockAccForm.getTempPwd();
		String encryptedPwd = null;

		encryptedPwd = PwdUtils.encryptMsg(tempPwd);

		UserAccountEntity user = userRepo.findByEmailAndPazzword(email, encryptedPwd);
		if (user != null) {
			String newPwd = unlockAccForm.getNewPwd();
			String encryptedNewPwd = null;
			encryptedNewPwd = PwdUtils.encryptMsg(newPwd);
			user.setPazzword(encryptedNewPwd);

			user.setAccStatus(AppConstants.UNLOCKED);
			userRepo.save(user);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean forgotPwd(String emailId) throws UserAppException {
		UserAccountEntity entity = new UserAccountEntity();
		entity.setEmail(emailId);

		Example<UserAccountEntity> example = Example.of(entity);

		Optional<UserAccountEntity> findOne = userRepo.findOne(example);

		if (findOne.isPresent()) {
			UserAccountEntity userEntity = findOne.get();

			String body = readForgotPwdEmailBody(userEntity);
			String subject = appProps.getMessages().get(AppConstants.RECOVER_PWD_EMAIL_SUB);
			emailUtils.sendEmail(userEntity.getEmail(), subject, body);
			return true;
		} else {
			return false;
		}
	}

	private String readForgotPwdEmailBody(UserAccountEntity entity) throws FileMissingException {

		StringBuilder sb = new StringBuilder(AppConstants.EMPTY_STR);
		String mailBody = AppConstants.EMPTY_STR;
		try {
			String fileName = appProps.getMessages().get(AppConstants.RECOVER_PWD_EMAIL_BODY_FILE);
			FileReader fr = new FileReader(fileName);

			BufferedReader br = new BufferedReader(fr);

			String line = br.readLine();
			while (line != null) {
				sb.append(line);
				line = br.readLine();
			}
			fr.close();
			br.close();

			String pazzword = entity.getPazzword();
			String decryptedPwd = PwdUtils.decryptMsg(pazzword);

			mailBody = sb.toString();

			mailBody = mailBody.replace(AppConstants.FNAME, entity.getFname());
			mailBody = mailBody.replace(AppConstants.LNAME, entity.getLname());
			mailBody = mailBody.replace(AppConstants.PWD, decryptedPwd);
		} catch (Exception e) {
			throw new FileMissingException(e.getMessage());
		}

		return mailBody;

	}

	@Override
	public String loginCheck(LoginForm loginForm) throws UserAppException {
		String msg;
		String encryptedPwd = null;
		encryptedPwd = PwdUtils.encryptMsg(loginForm.getPwd());

		UserAccountEntity user = userRepo.findByEmailAndPazzword(loginForm.getEmail(), encryptedPwd);

		if (user != null) {
			String accStatus = user.getAccStatus();
			if (AppConstants.LOCKED.equals(accStatus)) {

				msg = appProps.getMessages().get(AppConstants.ACC_LOCKED);

			} else {
				msg = AppConstants.SUCCESS;
			}
		} else {
			msg = appProps.getMessages().get(AppConstants.INVALID_CREDENTIAILS);
		}

		return msg;
	}

}
