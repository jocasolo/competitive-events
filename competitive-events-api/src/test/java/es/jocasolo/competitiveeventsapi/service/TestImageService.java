package es.jocasolo.competitiveeventsapi.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;

import es.jocasolo.competitiveeventsapi.dao.ImageDAO;
import es.jocasolo.competitiveeventsapi.dto.image.ImageDTO;
import es.jocasolo.competitiveeventsapi.enums.ImageType;
import es.jocasolo.competitiveeventsapi.exceptions.image.ImageNotFoundException;
import es.jocasolo.competitiveeventsapi.model.Image;

@RunWith(MockitoJUnitRunner.class)
class TestImageService {
	
	@InjectMocks
	private ImageService imageService = new ImageServiceImpl();
	
	@Mock
	private CommonService commonService = new CommonServiceImpl();
	
	@Mock
	private ImageDAO imageDao;
	
	@Mock
	private AmazonS3 s3client;
	
	private static final String IMAGE_ID = "image-text.jpg";
	
	Image mockedImage = new Image();
	private ImageDTO dto = new ImageDTO();
	private MultipartFile multipartFile;
	
	@BeforeEach
	void init() {
		
		MockitoAnnotations.initMocks(this);
		
		mockedImage.setId(1);
		mockedImage.setStorageId(IMAGE_ID);
		dto.setId(IMAGE_ID);
		dto.setName(IMAGE_ID);
		dto.setUrl("http://www.image.es");
		
		File file = new File("src/test/resources/test-image.png");
		try (FileInputStream input = new FileInputStream(file)) {
			multipartFile = new MockMultipartFile("file", file.getName(), "image/png", input.readAllBytes());
		} catch (IOException e) {
		} 
		
		Mockito.when(imageDao.findOne(IMAGE_ID)).thenReturn(mockedImage);
		Mockito.when(imageService.upload(multipartFile, ImageType.EVENT)).thenReturn(dto);
	}
	
	@Test
	void testFindOne() throws ImageNotFoundException {
		Image e = imageService.findOne(IMAGE_ID);
		assertNotNull(e);
		assertEquals(1, e.getId());
		
		assertThrows(ImageNotFoundException.class, () -> {
			imageService.findOne("don't exists");
	    });
	}
	
	@Test
	void testDelete() throws ImageNotFoundException{
		imageService.delete(IMAGE_ID);
		assertThrows(ImageNotFoundException.class, () -> {
			imageService.delete("don't exists");
	    });
	}
	
	@Test
	void testUpload() {
		ImageDTO imageDTO = imageService.upload(multipartFile, ImageType.EVENT);
		assertNotNull(imageDTO);
		assertEquals(IMAGE_ID, imageDTO.getId());
		assertEquals("http://www.image.es", imageDTO.getUrl());
		
	}
	
	
}