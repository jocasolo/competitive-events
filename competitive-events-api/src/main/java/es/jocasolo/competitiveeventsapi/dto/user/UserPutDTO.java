package es.jocasolo.competitiveeventsapi.dto.user;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.Email;

import es.jocasolo.competitiveeventsapi.dto.DTO;

public class UserPutDTO extends DTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@Email
	private String email;

	private String name;

	private String surname;

	private String description;

	private Date birthDate;

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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	@Override
	public String toString() {
		return "UserPostDTO [email=" + email + "]";
	}

}
