package es.jocasolo.competitiveeventsapi.dto.user;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

public class UserCompleteDTO extends UserDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	private String email;

	@Override
	public String toString() {
		return "UserCompleteDTO [id=" + super.getId() + "]";
	}

}
