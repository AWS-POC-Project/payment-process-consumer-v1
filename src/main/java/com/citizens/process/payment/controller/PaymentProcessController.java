/**
 * 
 */
package com.citizens.process.payment.controller;

import java.io.IOException;

import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.citizens.process.payment.consumer.PaymentProcessConsumer;
import com.citizens.process.payment.error.PaymentProcessException;
import com.citizens.process.payment.model.PaymentProcess;


/**
 * @author Sathish_G01
 *
 */
@RestController
@RequestMapping(value = "/send")
public class PaymentProcessController {
	
	@Autowired
	PaymentProcessConsumer paymentProcessConsumer;
	
	@Autowired
	private KieSession session;
	
	
	@PostMapping(value = "/fireRules")
	public PaymentProcess fireRules(@RequestBody final PaymentProcess paymentProcessReq) throws IOException
	{
		 session.insert(paymentProcessReq);
		 session.fireAllRules();
		 if(paymentProcessReq !=null &&
					!"email".equalsIgnoreCase(paymentProcessReq.getChannel()))
				throw new PaymentProcessException("Notification type not available for the LOB provided");
 
		 return paymentProcessReq;
	}
	
	@PostMapping(value = "/notify")
	public PaymentProcess sendNotification(@RequestBody final PaymentProcess paymentProcessReq) throws IOException
	{
		paymentProcessConsumer.sendNotification(paymentProcessReq);		
		return paymentProcessReq;
	}
	

}
