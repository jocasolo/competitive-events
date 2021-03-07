package es.jocasolo.competitiveeventsapi.service;
import java.io.IOException;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

@Component
public class EmailServiceImpl implements EmailService {
	
	private static final Logger log = LoggerFactory.getLogger(EmailServiceImpl.class);

    @Autowired
    private JavaMailSender sender;
    
    @Autowired
    private Configuration freemarkerConfig;

    public void sendSimpleMessage(String to, String subject, String templateFile, Map<String, Object> parameters) {
    	
    	MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        // Using a subfolder such as /templates here
        freemarkerConfig.setClassForTemplateLoading(this.getClass(), "/templates");
        
		try {
			Template template = freemarkerConfig.getTemplate(templateFile);
			final String text = FreeMarkerTemplateUtils.processTemplateIntoString(template, parameters);

	        helper.setTo(to);
	        helper.setText(text, true);
	        helper.setSubject(subject);
	        helper.setFrom("jocasolo.test@gmail.com");

	        sender.send(message);
	        
		} catch (IOException | TemplateException | MessagingException e) {
			log.error("Error sending email.", e);
		}
        
    }
}