package es.jocasolo.competitiveeventsapi.dto.user;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.Email;

import es.jocasolo.competitiveeventsapi.dto.DTO;
import lombok.Getter;
import lombok.Setter;

public class UserPutDTO extends DTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	@Email
	private String email;
	
	@Getter
	@Setter
	private String phone;

	@Getter
	@Setter
	private String name;

	@Getter
	@Setter
	private String surname;

	@Getter
	@Setter
	private String description;

	@Getter
	@Setter
	private Date birthDate;

	@Override
	public String toString() {
		return "UserPostDTO [email=" + email + "]";
	}

}
