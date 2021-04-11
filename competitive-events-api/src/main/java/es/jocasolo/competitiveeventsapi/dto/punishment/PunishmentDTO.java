package es.jocasolo.competitiveeventsapi.dto.punishment;

import java.io.Serializable;

import es.jocasolo.competitiveeventsapi.dto.DTO;
import es.jocasolo.competitiveeventsapi.dto.image.ImageDTO;
import es.jocasolo.competitiveeventsapi.dto.user.UserDTO;
import es.jocasolo.competitiveeventsapi.enums.score.ScoreSortType;

public class PunishmentDTO extends DTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id;

	private String title;

	private String description;

	private UserDTO looser;

	private ScoreSortType sortScore;

	private Integer requiredPosition;

	private ImageDTO image;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public UserDTO getLooser() {
		return looser;
	}

	public void setLooser(UserDTO looser) {
		this.looser = looser;
	}

	public ScoreSortType getSortScore() {
		return sortScore;
	}

	public void setSortScore(ScoreSortType sortScore) {
		this.sortScore = sortScore;
	}

	public Integer getRequiredPosition() {
		return requiredPosition;
	}

	public void setRequiredPosition(Integer requiredPosition) {
		this.requiredPosition = requiredPosition;
	}

	public ImageDTO getImage() {
		return image;
	}

	public void setImage(ImageDTO image) {
		this.image = image;
	}

	@Override
	public String toString() {
		return String.format("RewardDTO [id=%s, title=%s]", id, title);
	}

}