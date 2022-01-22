package es.jocasolo.competitiveeventsapi.mappers;

import org.springframework.beans.factory.annotation.Autowired;

import es.jocasolo.competitiveeventsapi.dto.event.EventDetailDTO;
import es.jocasolo.competitiveeventsapi.dto.image.ImageDTO;
import es.jocasolo.competitiveeventsapi.model.Event;
import es.jocasolo.competitiveeventsapi.service.CommonService;

public class EventMapperImpl implements EventMapper {
	
	@Autowired
	private CommonService commonService;
	
	public EventDetailDTO map(Event event) {
		
		EventDetailDTO detail = new EventDetailDTO();
		
		detail.setApprovalNeeded(event.getApprovalNeeded());
		detail.setDescription(event.getDescription());
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
		detail.setUsers(null);
		
		return detail;
		
	}

}
