package com.citizens.process.payment.controller;

import java.util.Date;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.citizens.process.payment.error.ErrorMessage;
import com.citizens.process.payment.error.PaymentProcessException;



@ControllerAdvice
public class AppExceptionHandler extends ResponseEntityExceptionHandler{
	
	@ExceptionHandler(value= {PaymentProcessException.class})
	public ResponseEntity<Object> handleExp(PaymentProcessException ex, WebRequest req)
	{
		ErrorMessage msg = new ErrorMessage();
		msg.setTimestamp(new Date());
		msg.setErrorCode("APP-101-LOB");
		msg.setErrorDesc(ex.getMessage());
		return new ResponseEntity<>(msg,  HttpStatus.BAD_REQUEST);
	}
}
