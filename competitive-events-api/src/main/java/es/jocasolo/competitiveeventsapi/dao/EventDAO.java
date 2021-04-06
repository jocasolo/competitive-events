package es.jocasolo.competitiveeventsapi.dao;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import es.jocasolo.competitiveeventsapi.enums.event.EventInscriptionType;
import es.jocasolo.competitiveeventsapi.enums.event.EventStatusType;
import es.jocasolo.competitiveeventsapi.enums.event.EventType;
import es.jocasolo.competitiveeventsapi.model.Event;
import es.jocasolo.competitiveeventsapi.model.User;

@Repository
public interface EventDAO extends CrudRepository<Event, String> {

	/**
	 * Search for a event by id.
	 * 
	 * @param id
	 * @return Event corresponding to the id searched.
	 */
	@Query(value = "SELECT e FROM Event AS e WHERE id = :id AND status <> 'DELETED'")
	public Event findOne(@Param("id") String id);
	
	@Query(value = "SELECT e from Event as e WHERE "
			+ "(e.title LIKE %:title% OR :title IS NULL) "
			+ "AND (e.type LIKE :type OR :type IS NULL) "
			+ "AND (e.status LIKE :status OR :status IS NULL) "
			+ "AND (e.inscription LIKE :inscription OR :inscription IS NULL) AND e.visibility = 'PUBLIC' AND status <> 'DELETED'"
		)
	public Page<Event> search(@Param("title") String title, @Param("type") EventType type, @Param("status") EventStatusType status, 
			@Param("inscription") EventInscriptionType inscription, Pageable pageRequest);
	
	@Query(value = "SELECT e from Event as e INNER JOIN EventUser as eu ON e.id = eu.event WHERE "
			+ "(eu.user = :user) "
			+ "AND (e.title LIKE %:title% OR :title IS NULL) "
			+ "AND (e.type LIKE :type OR :type IS NULL) "
			+ "AND (e.status LIKE :status OR :status IS NULL) "
			+ "AND (e.inscription LIKE :inscription OR :inscription IS NULL) AND status <> 'DELETED'"
		)
	public Page<Event> searchByUser(@Param("title") String title, @Param("type") EventType type, @Param("status") EventStatusType status, 
			@Param("inscription") EventInscriptionType inscription, @Param("user") User user, Pageable pageRequest);
	
	@Query(value = "SELECT e FROM Event AS e WHERE status = 'NOT_ACTIVE' AND init_date < CURRENT_DATE")
	public List<Event> getPendingActive();

	@Query(value = "SELECT e FROM Event AS e LEFT JOIN FETCH e.rewards LEFT JOIN FETCH e.punishments WHERE e.status = 'ACTIVE' AND e.endDate < CURRENT_DATE")
	public Set<Event> getPendingFinish();
	
}
