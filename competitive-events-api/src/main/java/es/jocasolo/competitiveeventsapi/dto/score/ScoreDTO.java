package es.jocasolo.competitiveeventsapi.dto.score;

import java.io.Serializable;

import es.jocasolo.competitiveeventsapi.dto.DTO;
import es.jocasolo.competitiveeventsapi.dto.image.ImageDTO;
import es.jocasolo.competitiveeventsapi.enums.score.ScoreStatusType;

public class ScoreDTO extends DTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;

	private String value;

	private String date;

	private ScoreStatusType status;

	private ImageDTO image;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public ScoreStatusType getStatus() {
		return status;
	}

	public void setStatus(ScoreStatusType status) {
		this.status = status;
	}

	public ImageDTO getImage() {
		return image;
	}

	public void setImage(ImageDTO image) {
		this.image = image;
	}

	@Override
	public String toString() {
		return String.format("ScoreDTO [id=%s, value=%s]", id, value);
	}

}
