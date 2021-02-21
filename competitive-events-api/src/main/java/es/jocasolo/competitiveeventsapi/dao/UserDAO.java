package es.jocasolo.competitiveeventsapi.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import es.jocasolo.competitiveeventsapi.model.user.User;

@Repository
public interface UserDAO extends CrudRepository<User, String> {
	
	/**
	 * Search for a user by identifier.
	 * @param code
	 * @return Event corresponding to the id searched.
	 */
	@Query(value = "SELECT u FROM User AS u WHERE identifier = :identifier")
	public User findOne(@Param("identifier") String identifier);
	
	/**
	 * Search for a user by email.
	 * @param code
	 * @return Event corresponding to the id searched.
	 */
	@Query(value = "SELECT u FROM User AS u WHERE email = :email")
	public User findOneByEmail(@Param("email") String email);
	
}
