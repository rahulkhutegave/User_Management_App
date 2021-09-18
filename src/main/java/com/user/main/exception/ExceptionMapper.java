package com.user.main.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionMapper {

	@ExceptionHandler(value = SmtpException.class)
	public ResponseEntity<ErrorResponse> handleSmptException(SmtpException e) {

		ErrorResponse response = new ErrorResponse();
		response.setErrorCode("Smtp100");
		response.setErrorMsg(e.getMessage());
		response.setDateTime(LocalDateTime.now());

		return new ResponseEntity<ErrorResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(value = UserAppException.class)
	public ResponseEntity<ErrorResponse> handleNoUserException(UserAppException ue) {
		ErrorResponse response = new ErrorResponse();
		response.setErrorCode("US101");
		response.setErrorMsg(ue.getMessage());
		response.setDateTime(LocalDateTime.now());

		return new ResponseEntity<ErrorResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(value = FileMissingException.class)
	public ResponseEntity<ErrorResponse> handleFileMissingException(FileMissingException fe) {
		ErrorResponse response = new ErrorResponse();
		response.setErrorCode("FM102");
		response.setErrorMsg(fe.getMessage());
		response.setDateTime(LocalDateTime.now());
		return new ResponseEntity<ErrorResponse>(response, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(value = PasswordEncryptDecryptException.class)
	public ResponseEntity<ErrorResponse> handlePwdEncDecException(PasswordEncryptDecryptException pe) {
		ErrorResponse response = new ErrorResponse();
		response.setErrorCode("PWD103");
		response.setErrorMsg(pe.getMessage());
		response.setDateTime(LocalDateTime.now());
		return new ResponseEntity<ErrorResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
