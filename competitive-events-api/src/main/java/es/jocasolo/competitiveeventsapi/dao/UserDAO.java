package es.jocasolo.competitiveeventsapi.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import es.jocasolo.competitiveeventsapi.model.user.User;

@Repository
public interface UserDAO extends CrudRepository<User, String> {
	
	/**
	 * Search for a user by username.
	 * @param username
	 * @return User corresponding to the username searched.
	 */
	@Query(value = "SELECT u FROM User AS u WHERE username = :username")
	public User findOne(@Param("username") String username);
	
	/**
	 * Search for a user by email.
	 * @param username
	 * @return User corresponding to the id searched.
	 */
	@Query(value = "SELECT u FROM User AS u WHERE email = :email")
	public User findOneByEmail(@Param("email") String email);
	
}
