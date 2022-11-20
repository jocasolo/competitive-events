package es.jocasolo.competitiveeventsapi.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.mail.internet.MimeMessage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import es.jocasolo.competitiveeventsapi.constants.CommonConstants;
import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;

@RunWith(MockitoJUnitRunner.class)
class TestEmailService {
	
	@InjectMocks
	private EmailService emailService = new EmailServiceImpl();
	
	@Mock
	private JavaMailSender sender;
	
	@Mock
	private Configuration freemarkerConfig;
	
	Map<String,Object> parameters = null;
	
	@BeforeEach
	void init() throws IOException, TemplateException {
		
		MockitoAnnotations.openMocks(this);
		
		MimeMessage message = Mockito.mock(MimeMessage.class);
		Template template = Mockito.mock(Template.class);
		parameters = new HashMap<>();
		parameters.put("key", "12345");
		
		// Mock static utils
		MockedStatic<FreeMarkerTemplateUtils> utilities = Mockito.mockStatic(FreeMarkerTemplateUtils.class);
	    utilities.when(() -> FreeMarkerTemplateUtils.processTemplateIntoString(template, parameters)).thenReturn("template");
	    
		Mockito.when(sender.createMimeMessage()).thenReturn(message);
		Mockito.when(freemarkerConfig.getTemplate(CommonConstants.EMAIL_CONFIRMATION_SUBJECT)).thenReturn(template);
		
	}
	
	@Test
	void testSendSimpleMessage() throws TemplateNotFoundException, MalformedTemplateNameException, ParseException, IOException{
		
		emailService.sendSimpleMessage("test@test.com", "Subject", CommonConstants.EMAIL_CONFIRMATION_SUBJECT, parameters);
	}
	

	
}
