package es.jocasolo.competitiveeventsapi.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import es.jocasolo.competitiveeventsapi.dao.ImageDAO;
import es.jocasolo.competitiveeventsapi.enums.ImageType;
import es.jocasolo.competitiveeventsapi.exceptions.image.ImageNotFoundException;
import es.jocasolo.competitiveeventsapi.exceptions.image.ImageUploadException;
import es.jocasolo.competitiveeventsapi.exceptions.user.UserNotValidException;
import es.jocasolo.competitiveeventsapi.model.Image;
import es.jocasolo.competitiveeventsapi.utils.security.AuthenticationFacade;
import io.minio.MinioClient;
import io.minio.RemoveObjectArgs;
import io.minio.UploadObjectArgs;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;

@Service
public class ImageServiceMinioImpl implements ImageService {

	private static final Logger log = LoggerFactory.getLogger(ImageServiceMinioImpl.class);

	@Autowired
	private ImageDAO imageDao;

	@Autowired
	private AuthenticationFacade authentication;

	private MinioClient minioClient;

	@Value("${minio.endpoint}")
	private String url;

	@Value("${minio.bucket-name}")
	private String bucketName;

	@Value("${minio.access-key}")
	private String accessKey;

	@Value("${minio.secret-key}")
	private String secretKey;

	@Value("${minio.enabled}")
	private boolean enabled;

	@PostConstruct
	private void init() {
		if (enabled) {
			this.minioClient = MinioClient.builder()
					.endpoint(url)
					.credentials(accessKey, secretKey)
					.build();
		}
	}

	@Override
	public Image findOne(String id) throws ImageNotFoundException {
		final Image image = imageDao.findOne(id);
		if (image == null)
			throw new ImageNotFoundException();

		return image;
	}

	@Override
	public Image upload(MultipartFile multipart, ImageType type) throws ImageUploadException {

		Optional<File> file = convertMultiPartToFile(multipart);
		if (file.isPresent()) {

			final String fileName = generateFileName(multipart);
			final String folder = type.name().toLowerCase();
			final String id = folder + "/" + fileName;

			uploadFileToS3bucket(id, file.get());

			final Image image = new Image();
			image.setUrl("/" + bucketName + "/" + id);
			image.setType(type);
			image.setFolder(folder);
			image.setName(fileName);
			image.setStorageId(id);
			image.setOwner(authentication.getUser());

			// Save
			return imageDao.save(image);
		}

		throw new ImageUploadException();
	}

	@Override
	public void delete(String id) throws ImageNotFoundException, UserNotValidException {
		final Image image = imageDao.findOne(id);
		if (image == null)
			throw new ImageNotFoundException();
		
		if(!image.getOwner().equals(authentication.getUser()))
			throw new UserNotValidException();

		imageDao.delete(image);
		
		try {
			minioClient.removeObject(RemoveObjectArgs.builder()
					.bucket(bucketName)
					.object(id)
					.build()
					);
		} catch (InvalidKeyException | ErrorResponseException | InsufficientDataException | InternalException | InvalidResponseException | NoSuchAlgorithmException
				| ServerException | XmlParserException | IllegalArgumentException | IOException e) {
			log.error("Failed to remove file {}", id, e);
		}

	}

	/**
	 * Convert a multipart file into a temporary file that will be deleted
	 * 
	 * @param multipartFile Multipart file
	 * @return Optional with the file or empty
	 */
	private Optional<File> convertMultiPartToFile(MultipartFile multipartFile) {
		Optional<File> optional = Optional.empty();

		try {
			File convFile = File.createTempFile(multipartFile.getOriginalFilename(), null, null);
			convFile.deleteOnExit();

			try (FileOutputStream fos = new FileOutputStream(convFile)) {
				fos.write(multipartFile.getBytes());
				optional = Optional.of(convFile);
			}

		} catch (IOException e) {
			log.error("Error creating image file.");
		}

		return optional;
	}

	/**
	 * Generates a unique file name with the current time and the real name
	 * 
	 * @param multiPart Multipart file
	 * @return File name
	 */
	private String generateFileName(MultipartFile multiPart) {
		String fileName = String.valueOf(new Date().getTime());
		final String multipartName = multiPart.getOriginalFilename();
		if (StringUtils.isNotEmpty(multipartName)) {
			fileName += "-" + multipartName.replace(" ", "_");
		}
		return fileName;
	}

	/**
	 * Upload the image to the storage system
	 * @param fileName File name
	 * @param file Temp file
	 */
	private void uploadFileToS3bucket(String fileName, File file) {
		try {
			minioClient.uploadObject(UploadObjectArgs.builder()
					.bucket(bucketName)
					.filename(file.getAbsolutePath())
					.object(fileName)
					.build());
		} catch (InvalidKeyException | ErrorResponseException | InsufficientDataException | InternalException | InvalidResponseException | NoSuchAlgorithmException
				| ServerException | XmlParserException | IllegalArgumentException | IOException e) {
			log.error("Failed to upload file {}", fileName, e);
		}
	}

}