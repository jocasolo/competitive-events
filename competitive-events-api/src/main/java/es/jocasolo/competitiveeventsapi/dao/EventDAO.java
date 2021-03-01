package es.jocasolo.competitiveeventsapi.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import es.jocasolo.competitiveeventsapi.model.event.Event;

@Repository
public interface EventDAO extends CrudRepository<Event, String> {

	/**
	 * Search for a event by id.
	 * 
	 * @param id
	 * @return Event corresponding to the id searched.
	 */
	@Query(value = "SELECT e FROM Event AS e WHERE id = :id")
	public Event findOne(@Param("id") String id);

	@Query(value = "SELECT e from Event as e WHERE ("
			+ "e.title LIKE %:title% OR :title IS NULL)")
	public Page<Event> search(@Param("title") String title, Pageable pageRequest);

}
