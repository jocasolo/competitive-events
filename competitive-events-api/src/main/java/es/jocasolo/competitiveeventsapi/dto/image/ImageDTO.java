package es.jocasolo.competitiveeventsapi.dto.image;

import java.io.Serializable;

import es.jocasolo.competitiveeventsapi.dto.DTO;

public class ImageDTO extends DTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id;

	private String name;

	private String url;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "ImageDTO [name=" + name + "]";
	}

}
