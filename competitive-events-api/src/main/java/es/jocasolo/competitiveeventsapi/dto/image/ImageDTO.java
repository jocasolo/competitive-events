package es.jocasolo.competitiveeventsapi.dto.image;

import java.io.Serializable;

import es.jocasolo.competitiveeventsapi.dto.DTO;
import lombok.Getter;
import lombok.Setter;

public class ImageDTO extends DTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	private Integer id;
	
	@Getter
	@Setter
	private String storageId;

	@Getter
	@Setter
	private String name;

	@Getter
	@Setter
	private String url;

	@Override
	public String toString() {
		return "ImageDTO [name=" + name + "]";
	}

}
