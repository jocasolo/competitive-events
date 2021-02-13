package es.jocasolo.competitiveeventsapi.service;

import org.springframework.web.multipart.MultipartFile;

import es.jocasolo.competitiveeventsapi.dto.image.ImageDTO;
import es.jocasolo.competitiveeventsapi.enums.ImageType;

public interface ImageService {
	
	ImageDTO upload(MultipartFile multipart, ImageType type);
	
	void delete(String folder, String name);

}
