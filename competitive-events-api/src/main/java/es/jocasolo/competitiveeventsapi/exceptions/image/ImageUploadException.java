package es.jocasolo.competitiveeventsapi.exceptions.image;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ImageUploadException extends Exception {

	private static final long serialVersionUID = 1L;
	private static final String MSG = "An error occurred while uploading the image.";
	
	@Override
	public String getMessage() {
		return MSG;
	}
	
}
