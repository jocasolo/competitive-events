package es.jocasolo.competitiveeventsapi.dto.user;

import java.io.Serializable;

public class UserCompleteDTO extends UserDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String email;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "UserCompleteDTO [id=" + super.getId() + "]";
	}

}
