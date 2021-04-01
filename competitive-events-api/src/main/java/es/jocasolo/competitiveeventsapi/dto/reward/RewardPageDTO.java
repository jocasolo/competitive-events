package es.jocasolo.competitiveeventsapi.dto.reward;

import java.io.Serializable;
import java.util.List;

import es.jocasolo.competitiveeventsapi.dto.DTO;

public class RewardPageDTO extends DTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long total;
	private Integer pages;
	private Boolean hasNext;
	private Boolean hasPrevious;
	private List<RewardDTO> events;

	public Long getTotal() {
		return total;
	}

	public void setTotal(Long total) {
		this.total = total;
	}

	public Boolean getHasNext() {
		return hasNext;
	}

	public void setHasNext(Boolean hasNext) {
		this.hasNext = hasNext;
	}

	public Boolean getHasPrevious() {
		return hasPrevious;
	}

	public void setHasPrevious(Boolean hasPrevious) {
		this.hasPrevious = hasPrevious;
	}

	public List<RewardDTO> getEvents() {
		return events;
	}

	public void setEvents(List<RewardDTO> events) {
		this.events = events;
	}

	public Integer getPages() {
		return pages;
	}

	public void setPages(Integer pages) {
		this.pages = pages;
	}

	@Override
	public String toString() {
		return "EventPageDTO [total=" + total + "]";
	}

}
