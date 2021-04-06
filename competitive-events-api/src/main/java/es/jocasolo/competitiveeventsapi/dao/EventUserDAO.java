package es.jocasolo.competitiveeventsapi.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import es.jocasolo.competitiveeventsapi.model.Event;
import es.jocasolo.competitiveeventsapi.model.EventUser;
import es.jocasolo.competitiveeventsapi.model.User;

@Repository
public interface EventUserDAO extends CrudRepository<EventUser, String> {

	/**
	 * Search for a event by id.
	 * 
	 * @param id Event id
	 * @param id User id
	 * @return EventUser corresponding to the id searched.
	 */
	@Query(value = "SELECT eu FROM EventUser AS eu WHERE event = :event AND user = :user AND status <> 'DELETED' AND status <> 'REJECTED'")
	public EventUser findOne(@Param("event") Event event, @Param("user") User user);
	
	/**
	 * Search for a event by id.
	 * 
	 * @param id Event id
	 * @param id User id
	 * @return EventUser corresponding to the id searched.
	 */
	@Query(value = "SELECT eu FROM EventUser AS eu WHERE event = :event AND user = :user")
	public EventUser findOneAllStatus(@Param("event") Event event, @Param("user") User user);
	
	/**
	 * Search for a event by id.
	 * 
	 * @param id Event id
	 * @param id User id
	 * @return EventUser corresponding to the id searched.
	 */
	@Query(value = "SELECT eu FROM EventUser AS eu WHERE event_id = :eventId AND user_id = :userId AND status <> 'DELETED' AND status <> 'REJECTED'")
	public EventUser findOneByIds(@Param("eventId") String eventId, @Param("userId") String userId);

}
