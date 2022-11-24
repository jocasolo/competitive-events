package es.jocasolo.competitiveeventsapi.model;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
class TestEvent {
	
	private Event event = new Event();
	
	@BeforeEach
	void init() {
		
		MockitoAnnotations.openMocks(this);
		
		Image image = new Image();
		image.setId(1);
		
		// event
		event.setId("test1");
		event.setApprovalNeeded(false);
		event.setTitle("title");
		event.setImages(Set.of(image));
		
	}
	
	 @Test
	 void testIsInDateRange() {
		 Date now = new Date();
		 assertTrue(event.isInDateRange());
		 
		 event.setEndDate(new Date(now.getTime()+10000));
		 assertTrue(event.isInDateRange());
		 
		 event.setEndDate(null);
		 event.setInitDate(new Date(now.getTime()-10000));
		 assertTrue(event.isInDateRange());
		 
		 event.setEndDate(new Date(now.getTime()+10000));
		 event.setInitDate(new Date(now.getTime()-10000));
		 assertTrue(event.isInDateRange());
	 }
	 
	 @Test
	 void testGetters() {
		 event.setComments(List.of(new Comment()));
		 assertFalse(event.getComments().isEmpty());
		 assertEquals("Event [id=test1]", event.toString());
		 assertFalse(event.getImages().isEmpty());
	 }
	
}
