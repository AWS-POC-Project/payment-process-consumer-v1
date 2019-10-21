/**
 * 
 */
package com.citizens.process.payment.service;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.citizens.process.payment.consumer.PaymentProcessConsumer;
import com.citizens.process.payment.model.PaymentProcess;

/**
 * @author Sathish_G01
 *
 */
@Component
public class SmtpMailSender {

	private final Logger logger = LoggerFactory.getLogger(SmtpMailSender.class);

	@Value("${from.id}")
	private String fromID;
	
	@Value("${payment.process.emails}")
	private String emailList;
	
	@Autowired
	private JavaMailSender javaMailSender;  

	@Autowired 
	private TemplateEngine templateEngine;
	 
	public void sendSimpleMail(PaymentProcess paymentProcessReq) 
	            throws MessagingException, IOException {

	        final Context ctx = new Context();
	        
	        if(paymentProcessReq.getCardMemberName() !=null)
	        	ctx.setVariable("cardMemberName", paymentProcessReq.getCardMemberName());
	        
	        if(paymentProcessReq.getStatementMonth() !=null)
	        	ctx.setVariable("statementMonth", paymentProcessReq.getStatementMonth());
	        
	        if(paymentProcessReq.getMinDue() !=null)
	        	ctx.setVariable("minDue", paymentProcessReq.getMinDue());
	        
	        if(paymentProcessReq.getStatementBalance() !=null)
	        	ctx.setVariable("statementBalance", paymentProcessReq.getStatementBalance());
	        
	        if(paymentProcessReq.getPaymentDueDate() !=null)
	        	ctx.setVariable("paymentDueDate", paymentProcessReq.getPaymentDueDate());
	       
	        final MimeMessage mimeMessage = this.javaMailSender.createMimeMessage();
	        final MimeMessageHelper message = new MimeMessageHelper(mimeMessage, "UTF-8");
	        message.setSubject(paymentProcessReq.getEmailSub());
	        message.setFrom(fromID);

	        if(emailList !=null)
	        {
	        	String[] arrSplit = emailList.split(",");
	        	message.setTo(arrSplit);

	        }
	
	        final String htmlContent = this.templateEngine.process("citizens-payment-template", ctx);
	        message.setText(htmlContent, true );

	        logger.info((String)mimeMessage.getContent());
	        this.javaMailSender.send(mimeMessage);

	    }
}
