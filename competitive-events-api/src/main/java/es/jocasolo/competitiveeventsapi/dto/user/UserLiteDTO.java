package es.jocasolo.competitiveeventsapi.dto.user;

import java.io.Serializable;

import es.jocasolo.competitiveeventsapi.dto.DTO;
import es.jocasolo.competitiveeventsapi.dto.image.ImageDTO;
import lombok.Getter;
import lombok.Setter;

public class UserLiteDTO extends DTO implements Serializable {

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
	private ImageDTO avatar;

	@Override
	public String toString() {
		return "UserLiteDTO [id=" + id + "]";
	}

}
