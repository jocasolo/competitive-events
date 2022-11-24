package es.jocasolo.competitiveeventsapi.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import es.jocasolo.competitiveeventsapi.dto.event.EventDTO;
import es.jocasolo.competitiveeventsapi.dto.image.ImageDTO;
import es.jocasolo.competitiveeventsapi.enums.event.EventInscriptionType;
import es.jocasolo.competitiveeventsapi.enums.event.EventStatusType;
import es.jocasolo.competitiveeventsapi.enums.event.EventType;
import es.jocasolo.competitiveeventsapi.enums.event.EventVisibilityType;
import es.jocasolo.competitiveeventsapi.enums.score.ScoreValueType;

@RunWith(MockitoJUnitRunner.class)
class TestEventDTO {

	private ImageDTO image = new ImageDTO();
	private EventDTO event = new EventDTO();

	@BeforeEach
	void init() {

		MockitoAnnotations.openMocks(this);

		image.setId(1);
		
		event.setId("event");
		event.setSubtitle("subtitle");
		event.setType(EventType.VIDEOGAMES);
		event.setInscription(EventInscriptionType.PUBLIC);
		event.setMaxPlaces(2);
		event.setScoreType(ScoreValueType.NUMERIC);
		event.setStatus(EventStatusType.FINISHED);
		event.setVisibility(EventVisibilityType.PRIVATE);
		
	}

	@Test
	void testCommon() {

		Date now = new Date();	
		event.setInitDate(now);
		event.setEndDate(now);
		
		assertEquals("subtitle", event.getSubtitle());
		assertEquals(EventType.VIDEOGAMES, event.getType());
		assertEquals(EventInscriptionType.PUBLIC, event.getInscription());
		assertEquals(EventVisibilityType.PRIVATE, event.getVisibility());
		assertEquals(now, event.getInitDate());
		assertEquals(now, event.getEndDate());
		assertEquals(2, event.getMaxPlaces());
		assertEquals(ScoreValueType.NUMERIC, event.getScoreType());
		assertEquals(EventStatusType.FINISHED, event.getStatus());
		System.out.print(event.toString());
		assertEquals("EventDTO [id=event]", event.toString());
		
	}

}
