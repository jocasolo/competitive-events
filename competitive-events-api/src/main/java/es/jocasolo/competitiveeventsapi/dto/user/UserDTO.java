package es.jocasolo.competitiveeventsapi.dto.user;

import java.io.Serializable;

import es.jocasolo.competitiveeventsapi.dto.DTO;

public class UserDTO extends DTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String username;

	private String name;

	private String surname;

	private String description;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "UserDTO [username=" + username + "]";
	}

}
