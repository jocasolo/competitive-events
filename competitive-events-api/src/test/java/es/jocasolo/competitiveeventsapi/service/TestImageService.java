package es.jocasolo.competitiveeventsapi.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import es.jocasolo.competitiveeventsapi.dao.ImageDAO;
import es.jocasolo.competitiveeventsapi.enums.ImageType;
import es.jocasolo.competitiveeventsapi.exceptions.image.ImageNotFoundException;
import es.jocasolo.competitiveeventsapi.exceptions.image.ImageUploadException;
import es.jocasolo.competitiveeventsapi.exceptions.user.UserNotValidException;
import es.jocasolo.competitiveeventsapi.model.Image;
import es.jocasolo.competitiveeventsapi.model.User;
import es.jocasolo.competitiveeventsapi.utils.security.AuthenticationFacade;
import io.minio.MinioClient;
import io.minio.RemoveObjectArgs;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;

@RunWith(MockitoJUnitRunner.class)
class TestImageService {

	@InjectMocks
	private ImageService imageService = new ImageServiceMinioImpl();

	@Mock
	private AuthenticationFacade authentication;
	
	@Mock
	private ImageDAO imageDao;

	private static final String IMAGE_ID = "image-text.jpg";

	private Image mockedImage = new Image();
	private Image img = new Image();
	private MultipartFile multipartFile;
	private User user = new User();

	@BeforeEach
	void init() throws ImageUploadException, InvalidKeyException, ErrorResponseException, InsufficientDataException, InternalException, InvalidResponseException, NoSuchAlgorithmException, ServerException, XmlParserException, IOException {

		MockitoAnnotations.openMocks(this);
		
		user.setId("user1");

		mockedImage.setId(1);
		mockedImage.setStorageId(IMAGE_ID);
		mockedImage.setOwner(user);
		img.setId(1);
		img.setName(IMAGE_ID);
		img.setUrl("http://www.image.es");
		img.setOwner(user);
		img.setStorageId(IMAGE_ID);

		File file = new File("src/test/resources/test-image.png");
		try (FileInputStream input = new FileInputStream(file)) {
			multipartFile = new MockMultipartFile("file", file.getName(), "image/png", input.readAllBytes());
		} catch (IOException e) {
		}
		
		MinioClient client = Mockito.mock(MinioClient.class);
		ReflectionTestUtils.setField(imageService, "bucketName", "competitive-events");
		ReflectionTestUtils.setField(imageService, "minioClient", client);
		
		Mockito.when(imageDao.findOne(IMAGE_ID)).thenReturn(mockedImage);
		Mockito.when(imageService.upload(multipartFile, ImageType.EVENT)).thenReturn(img);
		Mockito.when(authentication.getUser()).thenReturn(user);
		Mockito.when(imageDao.save(Mockito.any())).thenReturn(img);
		Mockito.doNothing().when(client).removeObject(Mockito.any());
		
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
	void testDelete() throws ImageNotFoundException, UserNotValidException {
		imageService.delete(IMAGE_ID);
		assertThrows(ImageNotFoundException.class, () -> {
			imageService.delete("don't exists");
		});
	}

	@Test
	void testUpload() throws ImageUploadException {
		Image image = imageService.upload(multipartFile, ImageType.EVENT);
		assertNotNull(image);
		assertEquals(IMAGE_ID, image.getStorageId());
		assertEquals("http://www.image.es", image.getUrl());

	}
}
