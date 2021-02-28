package es.jocasolo.competitiveeventsapi.dto.user;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import es.jocasolo.competitiveeventsapi.dto.DTO;
import es.jocasolo.competitiveeventsapi.utils.security.ValidPassword;

public class UserPasswordDTO extends DTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@NotNull
	private String username;

	@ValidPassword
	@NotNull
	private String password;

	@ValidPassword
	@NotNull
	private String newPassword;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	@Override
	public String toString() {
		return "UserPostDTO [username=" + username + "]";
	}

}
