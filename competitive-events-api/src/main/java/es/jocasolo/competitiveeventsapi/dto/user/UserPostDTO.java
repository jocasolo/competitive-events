package es.jocasolo.competitiveeventsapi.dto.user;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import es.jocasolo.competitiveeventsapi.dto.DTO;
import es.jocasolo.competitiveeventsapi.utils.security.ValidPassword;
import lombok.Getter;
import lombok.Setter;

public class UserPostDTO extends DTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	@NotBlank
	private String id;

	@Getter
	@Setter
	@NotBlank
	@Email
	private String email;

	@Getter
	@Setter
	@NotBlank
	@ValidPassword
	private String password;
	
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
		return "UserPostDTO [id=" + id + "]";
	}

}
