package es.jocasolo.competitiveeventsapi.dto.event;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import es.jocasolo.competitiveeventsapi.dto.comment.CommentDTO;
import es.jocasolo.competitiveeventsapi.dto.punishment.PunishmentDTO;
import es.jocasolo.competitiveeventsapi.dto.reward.RewardDTO;
import es.jocasolo.competitiveeventsapi.dto.score.ScoreDTO;
import es.jocasolo.competitiveeventsapi.dto.user.UserLiteDTO;
import lombok.Getter;
import lombok.Setter;

public class EventDetailDTO extends EventDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	private String description;
	
	@Getter
	@Setter
	private Set<UserLiteDTO> users;
	
	@Getter
	@Setter
	private Set<RewardDTO> rewards;
	
	@Getter
	@Setter
	private Set<PunishmentDTO> punishments;
	
	@Getter
	@Setter
	private List<ScoreDTO> scores;
	
	@Getter
	@Setter
	private List<CommentDTO> comments;

	@Override
	public String toString() {
		return "EventDetailDTO [id=" + super.getId() + "]";
	}

}
