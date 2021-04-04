package es.jocasolo.competitiveeventsapi.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import es.jocasolo.competitiveeventsapi.model.Comment;

@Repository
public interface CommentDAO extends CrudRepository<Comment, String> {

	/**
	 * @param id
	 * @return
	 */
	@Query(value = "SELECT c FROM Comment AS c WHERE id = :id")
	public Comment findOne(@Param("id") Integer id);
	
}
