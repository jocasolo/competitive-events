package es.jocasolo.competitiveeventsapi.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import es.jocasolo.competitiveeventsapi.dto.image.ImageDTO;
import es.jocasolo.competitiveeventsapi.enums.ImageType;
import es.jocasolo.competitiveeventsapi.service.CommonService;
import es.jocasolo.competitiveeventsapi.service.ImageService;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/image")
public class ImageController {
	
	private static final Logger log = LoggerFactory.getLogger(ImageController.class);
	
	@Autowired
	private CommonService commonService;
	
	@Autowired
	private ImageService imageService;
	
	@PostMapping
	@ApiOperation(value = "Upload an image")
	public ImageDTO upload(@RequestParam("file") MultipartFile file, @RequestParam("type") ImageType type) {
		log.debug("Uploading image");
		return commonService.transform(imageService.upload(file, type), ImageDTO.class);
	}
	
	@DeleteMapping(value = "/{folder}/{name}")
	@ApiOperation(value = "Delete an image")
	public void delete(@PathVariable("folder") String folder, @PathVariable("name") String name) {
		log.debug("Delete image {}/{}", folder, name);
		imageService.delete(folder, name);
	}

}
