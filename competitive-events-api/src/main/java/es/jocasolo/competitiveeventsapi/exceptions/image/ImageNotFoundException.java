package es.jocasolo.competitiveeventsapi.exceptions.image;

public class ImageNotFoundException extends Exception {

	private static final long serialVersionUID = 1L;
	private static final String MSG = "Image not found";
	
	@Override
	public String getMessage() {
		return MSG;
	}
	
}
