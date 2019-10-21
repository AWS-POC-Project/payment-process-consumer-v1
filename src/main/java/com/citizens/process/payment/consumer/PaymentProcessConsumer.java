/**
 * 
 */
package com.citizens.process.payment.consumer;

import java.io.IOException;
import java.util.Locale;
import javax.mail.MessagingException;

import org.kie.api.runtime.KieSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.citizens.process.payment.error.PaymentProcessException;
import com.citizens.process.payment.model.PaymentProcess;
import com.citizens.process.payment.service.SmtpMailSender;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Sathish_G01
 *
 */
@Service
public class PaymentProcessConsumer {
	
	private final Logger logger = LoggerFactory.getLogger(PaymentProcessConsumer.class);
	
	@Autowired
	SmtpMailSender mailSender;
	
	@Autowired
	private KieSession session;
	
	
	//@KafkaListener(topics = "payment-process", groupId="payment")
	public void consume(String message) throws JsonParseException, JsonMappingException, IOException, MessagingException
	{
		logger.info(String.format("$$ -> Consumed Message -> %s",message));
		
		PaymentProcess paymentProcessReq = new ObjectMapper().readValue(message, PaymentProcess.class);
		
		session.insert(paymentProcessReq);
		session.fireAllRules();
		
		sendNotification(paymentProcessReq);
		
	}
	
	public PaymentProcess sendNotification(PaymentProcess paymentProcessReq) throws IOException
	{
		
		if(paymentProcessReq !=null && paymentProcessReq.getChannel() !=null &&
				"email".equalsIgnoreCase(paymentProcessReq.getChannel()))
		{
			invokeEmailSender(paymentProcessReq);
			
		}
		else
		{
			throw new PaymentProcessException("Notification type not available for the LOB provided");
		}
		return paymentProcessReq;
	}
	
	public void invokeEmailSender(PaymentProcess paymentProcessReq) throws IOException
	{
		logger.info("Invoke eMail Flow for Consumer Banking");
		try {
			
			mailSender.sendSimpleMail(paymentProcessReq);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			logger.info("Unable to Invoke SMTP Server" + e.getMessage());
		}
	}
	
}
