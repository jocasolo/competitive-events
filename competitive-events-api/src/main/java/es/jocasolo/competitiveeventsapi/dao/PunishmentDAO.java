package es.jocasolo.competitiveeventsapi.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import es.jocasolo.competitiveeventsapi.model.event.Punishment;

@Repository
public interface PunishmentDAO extends CrudRepository<Punishment, String> {

	/**
	 * @param id
	 * @return
	 */
	@Query(value = "SELECT p FROM Punishment AS p WHERE id = :id")
	public Punishment findOne(@Param("id") Integer id);
	
}
