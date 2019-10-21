package com.citizens.process.payment.error;


public class PaymentProcessException extends RuntimeException {

	public PaymentProcessException(String msg)
	{
		super (msg);
	}
}
