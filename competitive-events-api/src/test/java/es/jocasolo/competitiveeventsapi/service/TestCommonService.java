package es.jocasolo.competitiveeventsapi.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.dozer.DozerBeanMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import es.jocasolo.competitiveeventsapi.dto.event.EventDTO;
import es.jocasolo.competitiveeventsapi.model.event.Event;

@RunWith(MockitoJUnitRunner.class)
class TestCommonService {
	
	@InjectMocks
	private CommonService commonService = new CommonServiceImpl();
	
	@Mock
	private DozerBeanMapper dozer;
	
	private Event event1 = new Event();
	private Event event2 = new Event();
	List<Event> events = new ArrayList<>();
	
	@BeforeEach
	void init() {
		
		MockitoAnnotations.initMocks(this);
		
		events.add(event1);
		events.add(event2);
		
		EventDTO dto1 = new EventDTO();
		dto1.setCode("1111");
		dto1.setTitle("Event 1");
		
		Mockito.when(dozer.map(event1, EventDTO.class)).thenReturn(dto1);
		
	}
	
	@Test
	void testTransform(){
		EventDTO eventTransformed = commonService.transform(event1, EventDTO.class);
		assertNotNull(eventTransformed);
		assertEquals("1111", eventTransformed.getCode());
		assertEquals("Event 1", eventTransformed.getTitle());
	}
	
	@Test
	void testTransformList(){
		final List<EventDTO> eventsTransformed = commonService.transform(events, EventDTO.class);
		assertEquals(2, eventsTransformed.size());
	}

}
