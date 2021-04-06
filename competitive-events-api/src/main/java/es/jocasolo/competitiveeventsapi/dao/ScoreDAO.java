package es.jocasolo.competitiveeventsapi.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import es.jocasolo.competitiveeventsapi.model.Event;
import es.jocasolo.competitiveeventsapi.model.Score;

@Repository
public interface ScoreDAO extends CrudRepository<Score, String> {

	/**
	 * @param id
	 * @return
	 */
	@Query(value = "SELECT s FROM Score AS s WHERE id = :id")
	public Score findOne(@Param("id") Integer id);
	
	/**
	 * @param id
	 * @return
	 */
	@Query(value = "SELECT s FROM Score AS s WHERE event = :event AND status = 'VALID' ORDER BY value")
	public Page<Score> search(@Param("event") Event event, Pageable pageRequest);
	
	/**
	 * @param id
	 * @return
	 */
	@Query(value = "SELECT s FROM Score AS s WHERE event = :event AND status = 'VALID' ORDER BY value ASC")
	public List<Score> findAllSortedAsc(@Param("event") Event event);
	
	/**
	 * @param id
	 * @return
	 */
	@Query(value = "SELECT s FROM Score AS s WHERE event = :event AND status = 'VALID' ORDER BY value DESC")
	public List<Score> findAllSortedDesc(@Param("event") Event event);
	
}
