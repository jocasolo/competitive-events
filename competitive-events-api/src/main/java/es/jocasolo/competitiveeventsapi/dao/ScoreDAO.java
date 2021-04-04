package es.jocasolo.competitiveeventsapi.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import es.jocasolo.competitiveeventsapi.model.Score;

@Repository
public interface ScoreDAO extends CrudRepository<Score, String> {

	/**
	 * @param id
	 * @return
	 */
	@Query(value = "SELECT s FROM Score AS s WHERE id = :id")
	public Score findOne(@Param("id") Integer id);
	
}
