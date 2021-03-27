package es.jocasolo.competitiveeventsapi.service;

import org.springframework.web.multipart.MultipartFile;

import es.jocasolo.competitiveeventsapi.enums.ImageType;
import es.jocasolo.competitiveeventsapi.exceptions.image.ImageNotFoundException;
import es.jocasolo.competitiveeventsapi.exceptions.image.ImageUploadException;
import es.jocasolo.competitiveeventsapi.exceptions.user.UserNotValidException;
import es.jocasolo.competitiveeventsapi.model.Image;

public interface ImageService {
	
	/**
	 * Search for a image by id.
	 * @param id Image id
	 * @return Image corresponding to the id searched.
	 * @throws ImageNotFoundException
	 */
	Image findOne(String id) throws ImageNotFoundException;
	
	/**
	 * Uploads a new image to the storage system and creates a image in bbdd table.
	 * @param multipart Multipart file
	 * @param type Image type
	 * @return ImageDTO
	 * @throws ImageUploadException 
	 */
	Image upload(MultipartFile multipart, ImageType type) throws ImageUploadException;
	
	/**
	 * Deletes an image from storage system and bbdd table.
	 * @param folder Image folder
	 * @param name Image name
	 * @throws UserNotValidException 
	 */
	void delete(String id) throws ImageNotFoundException, UserNotValidException;

}
