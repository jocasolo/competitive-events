package es.jocasolo.competitiveeventsapi.model.user;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonProperty;

import es.jocasolo.competitiveeventsapi.enums.user.UserStatusType;
import es.jocasolo.competitiveeventsapi.enums.user.UserType;
import es.jocasolo.competitiveeventsapi.model.event.Event;

@Entity
public class User implements UserDetails, Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	@Column(unique = true, nullable = false)
	private String email;

	@Enumerated(EnumType.STRING)
	private UserType type;

	@Enumerated(EnumType.STRING)
	private UserStatusType status;

	@Column(length = 1000)
	private String description;

	@Temporal(TemporalType.DATE)
	private Date registerDate;

	@Temporal(TemporalType.DATE)
	private Date birthDate;

	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(name = "event_user", joinColumns = { @JoinColumn(name = "user_id") }, inverseJoinColumns = { @JoinColumn(name = "event_id") })
	private Set<Event> events = new HashSet<>();

	@Column(nullable = false)
	private String password;

	private String name;

	private String surname;

	private Boolean confirmed;

	// GETTERS AND SETTERS

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	public UserType getType() {
		return type;
	}

	public void setType(UserType type) {
		this.type = type;
	}

	public Boolean getConfirmed() {
		return confirmed;
	}

	public void setConfirmed(Boolean confirmed) {
		this.confirmed = confirmed;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public Set<Event> getEvents() {
		return events;
	}

	public void setEvents(Set<Event> events) {
		this.events = events;
	}

	public UserStatusType getStatus() {
		return status;
	}

	public void setStatus(UserStatusType status) {
		this.status = status;
	}

	@JsonProperty("username")
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return String.format("User [id=%s]", id);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;

		if (this.getClass() != obj.getClass())
			return false;

		User user = (User) obj;
		return getId().equals(user.getId());
	}
	
	@Override
	public int hashCode() {
		return super.hashCode();
	}

	// USER DETAILS (Security)

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority(type.name()));
		return authorities;
	}

	@Override
	public String getUsername() {
		return getId();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return status.equals(UserStatusType.ACTIVE);
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return status.equals(UserStatusType.ACTIVE);
	}

}
