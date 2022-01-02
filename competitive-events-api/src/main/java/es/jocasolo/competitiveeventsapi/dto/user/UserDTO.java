package es.jocasolo.competitiveeventsapi.dto.user;

import java.io.Serializable;
import java.util.Date;

import es.jocasolo.competitiveeventsapi.dto.DTO;
import es.jocasolo.competitiveeventsapi.dto.image.ImageDTO;

public class UserDTO extends DTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id;

	private String name;

	private String surname;

	private String description;

	private ImageDTO avatar;

	private Date registerDate;

	private Date birthDate;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public ImageDTO getAvatar() {
		return avatar;
	}

	public void setAvatar(ImageDTO avatar) {
		this.avatar = avatar;
	}

	public Date getRegisterDate() {
		return registerDate;
	}

	public void setRegisterDate(Date registerDate) {
		this.registerDate = registerDate;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	@Override
	public String toString() {
		return "UserDTO [id=" + id + "]";
	}

}
