package es.jocasolo.competitiveeventsapi.dto.user;

import java.io.Serializable;
import java.util.List;

import es.jocasolo.competitiveeventsapi.dto.DTO;

public class UserPageDTO extends DTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long total;
	private Integer pages;
	private Boolean hasNext;
	private Boolean hasPrevious;
	private List<UserDTO> users;

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

	public List<UserDTO> getUsers() {
		return users;
	}

	public void setUsers(List<UserDTO> users) {
		this.users = users;
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
