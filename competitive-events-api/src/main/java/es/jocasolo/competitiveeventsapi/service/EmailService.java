package es.jocasolo.competitiveeventsapi.service;

import java.util.Map;

public interface EmailService {

	void sendSimpleMessage(String to, String subject, String templateFile, Map<String, Object> parameters);
	
}
