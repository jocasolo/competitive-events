package es.jocasolo.competitiveeventsapi.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import es.jocasolo.competitiveeventsapi.model.User;

@Repository
public interface UserDAO extends CrudRepository<User, String> {
	
	/**
	 * Search for a user by id.
	 * @param id
	 * @return User corresponding to the id searched.
	 */
	@Query(value = "SELECT u FROM User AS u WHERE id = :id AND status = 'ACTIVE'")
	public User findOne(@Param("id") String id);
	
	/**
	 * Search for a user by id.
	 * @param id
	 * @return User corresponding to the id searched.
	 */
	@Query(value = "SELECT u FROM User AS u WHERE phone = :phone AND status = 'ACTIVE'")
	public User findOneByPhone(@Param("phone") String phone);
	
	/**
	 * Search for a user by email.
	 * @param email User email
	 * @return User corresponding to the email searched.
	 */
	@Query(value = "SELECT u FROM User AS u WHERE email = :email")
	public User findOneByEmail(@Param("email") String email);
	
	/**
	 * Find one user by confirm key
	 * @param confirmKey Activation key
	 * @return The user with the key
	 */
	public List<User> findByConfirmKey(String confirmKey);

	@Query(value = "SELECT u from User as u WHERE "
			+ "(u.id LIKE %:id% OR :id IS NULL) "
		)
	public User search(String id);
	
}
