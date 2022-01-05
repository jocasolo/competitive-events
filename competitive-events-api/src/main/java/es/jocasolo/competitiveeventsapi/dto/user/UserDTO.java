package es.jocasolo.competitiveeventsapi.dto.user;

import java.io.Serializable;
import java.util.Date;

import es.jocasolo.competitiveeventsapi.dto.DTO;
import es.jocasolo.competitiveeventsapi.dto.image.ImageDTO;
import lombok.Getter;
import lombok.Setter;

public class UserDTO extends DTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	private String id;

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
	private ImageDTO avatar;

	@Getter
	@Setter
	private Date registerDate;

	@Getter
	@Setter
	private Date birthDate;

	@Override
	public String toString() {
		return "UserDTO [id=" + id + "]";
	}

}
