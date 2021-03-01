package es.jocasolo.competitiveeventsapi.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.dozer.Mapping;

import es.jocasolo.competitiveeventsapi.enums.ImageType;

@Entity
public class Image {

	@Id
	@GeneratedValue
	private Integer id;

	@Column(nullable = false)
	@Mapping("id")
	private String storageId;

	@Enumerated(EnumType.STRING)
	private ImageType type;

	private String folder;

	private String name;

	private String url;

	// GETTERS AND SETTERS

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getStorageId() {
		return storageId;
	}

	public void setStorageId(String storageId) {
		this.storageId = storageId;
	}

	public ImageType getType() {
		return type;
	}

	public void setType(ImageType type) {
		this.type = type;
	}

	public String getFolder() {
		return folder;
	}

	public void setFolder(String folder) {
		this.folder = folder;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	@Override
	public String toString() {
		return String.format("Image [id=%s]", id);
	}

}
