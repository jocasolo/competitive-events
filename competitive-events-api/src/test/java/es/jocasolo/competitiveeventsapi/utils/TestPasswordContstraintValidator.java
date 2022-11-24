package es.jocasolo.competitiveeventsapi.utils;

import static org.junit.Assert.assertTrue;

import javax.validation.ConstraintValidatorContext;
import javax.validation.ConstraintValidatorContext.ConstraintViolationBuilder;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import es.jocasolo.competitiveeventsapi.utils.security.PasswordConstraintValidator;

@RunWith(MockitoJUnitRunner.class)
class TestPasswordContstraintValidator {
	
	@BeforeEach
	void init() {
		
		MockitoAnnotations.openMocks(this);
		
	}
	
	@Test
	void testIsValid() {
		ConstraintValidatorContext context = Mockito.mock(ConstraintValidatorContext.class);
		ConstraintViolationBuilder builder = Mockito.mock(ConstraintViolationBuilder.class);
		PasswordConstraintValidator validator = new PasswordConstraintValidator();
		Mockito.when(context.buildConstraintViolationWithTemplate(Mockito.anyString())).thenReturn(builder);
		boolean result = validator.isValid("Test123456", context);
		assertTrue(result);
		
	}
}
