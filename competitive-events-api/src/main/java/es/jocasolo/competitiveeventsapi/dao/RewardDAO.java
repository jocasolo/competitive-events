package es.jocasolo.competitiveeventsapi.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import es.jocasolo.competitiveeventsapi.model.Reward;

@Repository
public interface RewardDAO extends CrudRepository<Reward, String> {

	/**
	 * @param id
	 * @return
	 */
	@Query(value = "SELECT r FROM Reward AS r WHERE id = :id")
	public Reward findOne(@Param("id") Integer id);
	
}
