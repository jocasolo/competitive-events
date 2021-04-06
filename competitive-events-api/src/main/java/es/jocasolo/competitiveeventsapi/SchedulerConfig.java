package es.jocasolo.competitiveeventsapi;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import es.jocasolo.competitiveeventsapi.dao.EventDAO;
import es.jocasolo.competitiveeventsapi.model.Event;
import es.jocasolo.competitiveeventsapi.service.EventService;

@Configuration
@EnableScheduling
public class SchedulerConfig {
	
	@Autowired
	private EventDAO eventDao;
	
	@Autowired
	private EventService eventService;

	@Scheduled(fixedDelay = 600000)
	public void scheduleFixedDelayTask() {
	    
		// Start events
		List<Event> eventsPendingActive = eventDao.getPendingActive();
		for(Event e : eventsPendingActive) {
			eventService.initEvent(e);
		}
		
		// Finish events
		Set<Event> eventsPendingFinish = eventDao.getPendingFinish();
		for(Event e : eventsPendingFinish) {
			e.getRewards();
			eventService.finishEvent(e);
		}
	}
	
}
