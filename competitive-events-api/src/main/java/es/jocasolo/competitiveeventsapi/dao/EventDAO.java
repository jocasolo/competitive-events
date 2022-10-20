package es.jocasolo.competitiveeventsapi.dao;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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
	
	@Query(nativeQuery=true, value = "SELECT * from Event as e WHERE "
			+ "(fuzzy_search(e.title, :title, 2) OR :title IS NULL) "
			+ "AND (e.type LIKE :type OR :type IS NULL) "
			+ "AND (e.status LIKE :status OR :status IS NULL) "
			+ "AND (e.inscription LIKE :inscription OR :inscription IS NULL) AND e.visibility = 'PUBLIC' AND status <> 'DELETED'"
		)
	public Page<Event> search(@Param("title") String title, @Param("type") String type, @Param("status") String status, 
			@Param("inscription") String inscription, Pageable pageRequest);
	
	@Query(nativeQuery=true, value = "SELECT * from Event as e INNER JOIN Event_User as eu ON e.id = eu.event_id WHERE "
			+ "(eu.user_id = :user) "
			+ "AND (fuzzy_search(e.title, :title, 2) OR :title IS NULL) "
			+ "AND (e.type LIKE :type OR :type IS NULL) "
			+ "AND (e.status LIKE :status OR :status IS NULL) "
			+ "AND (e.inscription LIKE :inscription OR :inscription IS NULL) AND eu.status = 'ACCEPTED' AND e.status <> 'DELETED'"
		)
	public Page<Event> searchByUser(@Param("title") String title, @Param("type") String type, @Param("status") String status, 
			@Param("inscription") String inscription, @Param("user") User user, Pageable pageRequest);
	
	@Query(value = "SELECT e FROM Event AS e WHERE status = 'NOT_ACTIVE' AND init_date < CURRENT_DATE")
	public List<Event> getPendingActive();

	@Query(value = "SELECT e FROM Event AS e LEFT JOIN FETCH e.rewards LEFT JOIN FETCH e.punishments WHERE e.status = 'ACTIVE' AND e.endDate < CURRENT_DATE")
	public Set<Event> getPendingFinish();
	
}
