package es.jocasolo.competitiveeventsapi.mappers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.jocasolo.competitiveeventsapi.dto.event.EventDTO;
import es.jocasolo.competitiveeventsapi.dto.image.ImageDTO;
import es.jocasolo.competitiveeventsapi.model.Event;
import es.jocasolo.competitiveeventsapi.service.CommonService;

@Service
public class EventMapperImpl implements EventMapper {
	
	@Autowired
	private CommonService commonService;
	
	public EventDTO map(Event event) {
		
		EventDTO detail = new EventDTO();
		
		detail.setApprovalNeeded(event.getApprovalNeeded());
		detail.setEndDate(event.getEndDate());
		detail.setId(event.getId());
		detail.setImage(commonService.transform(event.getImage(), ImageDTO.class));
		detail.setInitDate(event.getInitDate());
		detail.setInscription(event.getInscription());
		detail.setMaxPlaces(event.getMaxPlaces());
		detail.setScoreType(event.getScoreType());
		detail.setSubtitle(event.getSubtitle());
		detail.setTitle(event.getTitle());
		detail.setType(event.getType());
		detail.setNumParticipants(event.getUsers().size());
		detail.setVisibility(event.getVisibility());
		
		return detail;
	}
	
	public List<EventDTO> map(List<Event> events) {
		
		List<EventDTO> result = new ArrayList<>();
		
		for(Event event : events) {
			final EventDTO detail = map(event);
			result.add(detail);
		}
		
		return result;
		
	}

}
