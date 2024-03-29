/**
 * 
 */
package com.citizens.process.payment.config;

import java.io.IOException;

import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieModule;
import org.kie.api.builder.KieRepository;
import org.kie.api.builder.ReleaseId;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.internal.io.ResourceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * @author Sathish_G01
 *
 */
@Configuration
public class DroolConfig {
	
	private final Logger logger = LoggerFactory.getLogger(DroolConfig.class);

	KieServices kieServices = KieServices.Factory.get();

	private KieFileSystem getKieFileSystem() throws IOException {
			
		KieFileSystem kieFileSystem = kieServices.newKieFileSystem();
		
		kieFileSystem.write(ResourceFactory.newClassPathResource("rules/payment-process-rule.drl"));
		
		return kieFileSystem;
		
	}
	
	@Bean
	public KieContainer getKieContainer() throws IOException
	{
		getKieRepository();
		
		KieBuilder kb = kieServices.newKieBuilder(getKieFileSystem());
		
		kb.buildAll();
		
		KieModule km = kb.getKieModule();
		
		KieContainer kContainer = kieServices.newKieContainer(km.getReleaseId());
		
		return kContainer;
		
	}

	private void getKieRepository() 
	{
		final KieRepository kieRepository=  kieServices.getRepository();
		
		kieRepository.addKieModule(new KieModule() {
			
			@Override
			public ReleaseId getReleaseId() {
				return kieRepository.getDefaultReleaseId();
			}
		});
	}
	
	@Bean
	public KieSession getKieKession() throws IOException
	{
		logger.info("session created...");
		return getKieContainer().newKieSession();
	}
	
	
}
