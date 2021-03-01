package es.jocasolo.competitiveeventsapi.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import es.jocasolo.competitiveeventsapi.model.event.EventUser;

@Repository
public interface EventUserDAO extends CrudRepository<EventUser, String> {

	/**
	 * Search for a event by id.
	 * 
	 * @param id Event id
	 * @param id User id
	 * @return EventUser corresponding to the id searched.
	 */
	@Query(value = "SELECT e FROM EventUser AS e WHERE event_id = :eventId AND user_id = :userId")
	public EventUser findOne(@Param("eventId") String eventId, @Param("userId") String userId);

}
