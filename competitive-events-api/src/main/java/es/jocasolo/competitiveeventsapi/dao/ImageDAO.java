package es.jocasolo.competitiveeventsapi.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import es.jocasolo.competitiveeventsapi.model.Image;

@Repository
public interface ImageDAO extends CrudRepository<Image, String> {
	
	/**
	 * Search for a image by id.
	 * @param id
	 * @return Image corresponding to the id searched.
	 */
	@Query(value = "SELECT i FROM Image AS i WHERE storageId = :id")
	public Image findOne(@Param("id") String id);
	
}
