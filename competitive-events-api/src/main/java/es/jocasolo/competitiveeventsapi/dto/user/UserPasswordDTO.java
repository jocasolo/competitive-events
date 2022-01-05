package es.jocasolo.competitiveeventsapi.dto.user;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import es.jocasolo.competitiveeventsapi.dto.DTO;
import es.jocasolo.competitiveeventsapi.utils.security.ValidPassword;
import lombok.Getter;
import lombok.Setter;

public class UserPasswordDTO extends DTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	@NotNull
	private String id;

	@Getter
	@Setter
	@ValidPassword
	@NotNull
	private String password;

	@Getter
	@Setter
	@ValidPassword
	@NotNull
	private String newPassword;

	@Override
	public String toString() {
		return "UserPostDTO [id=" + id + "]";
	}

}
