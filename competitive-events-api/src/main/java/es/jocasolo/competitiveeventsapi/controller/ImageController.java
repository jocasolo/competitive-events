package es.jocasolo.competitiveeventsapi.controller;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.HandlerMapping;

import es.jocasolo.competitiveeventsapi.dto.image.ImageDTO;
import es.jocasolo.competitiveeventsapi.enums.ImageType;
import es.jocasolo.competitiveeventsapi.exceptions.image.ImageNotFoundException;
import es.jocasolo.competitiveeventsapi.exceptions.user.UserNotValidException;
import es.jocasolo.competitiveeventsapi.service.CommonService;
import es.jocasolo.competitiveeventsapi.service.ImageService;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/images")
public class ImageController {
	
	private static final Logger log = LoggerFactory.getLogger(ImageController.class);
	
	@Autowired
	private CommonService commonService;
	
	@Autowired
	private ImageService imageService;
	
	@GetMapping(value = "/**", produces = "application/json;charset=utf8")
	@ResponseStatus(HttpStatus.CREATED)
	@ApiOperation(value = "Search for an image based on its id.")
	public ImageDTO findOne(WebRequest request) throws ImageNotFoundException {
		String id = getIdFromPath(request);
		log.debug("Looking for the image with id: {}", id);
		return commonService.transform(imageService.findOne(id), ImageDTO.class);
	}
	
	@PostMapping(produces = "application/json;charset=utf8")
	@ApiOperation(value = "Upload an image")
	public ImageDTO upload(@RequestParam("file") MultipartFile file, @RequestParam("type") ImageType type) {
		log.debug("Uploading image");
		return commonService.transform(imageService.upload(file, type), ImageDTO.class);
	}
	
	@DeleteMapping(value = "/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@ApiOperation(value = "Delete an image")
	public void delete(@PathVariable("id") String id) throws ImageNotFoundException, UserNotValidException {
		log.debug("Delete image {}", id);
		imageService.delete(id);
	}
	
	private String getIdFromPath(WebRequest request) {
		String id = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE, RequestAttributes.SCOPE_REQUEST);
		if(StringUtils.isNotEmpty(id)) {
			id = id.replaceFirst("/image/", "");
		}
		return id;
	}

}
