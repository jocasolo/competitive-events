package es.jocasolo.competitiveeventsapi.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;

import es.jocasolo.competitiveeventsapi.dao.ImageDAO;
import es.jocasolo.competitiveeventsapi.enums.ImageType;
import es.jocasolo.competitiveeventsapi.exceptions.image.ImageNotFoundException;
import es.jocasolo.competitiveeventsapi.exceptions.image.ImageUploadException;
import es.jocasolo.competitiveeventsapi.exceptions.user.UserNotValidException;
import es.jocasolo.competitiveeventsapi.model.Image;
import es.jocasolo.competitiveeventsapi.utils.security.AuthenticationFacade;

@Service
public class ImageServiceImpl implements ImageService {

	private static final Logger log = LoggerFactory.getLogger(ImageServiceImpl.class);

	@Autowired
	private ImageDAO imageDao;

	@Autowired
	private AuthenticationFacade authentication;

	private AmazonS3 s3client;

	@Value("${amazon.s3.endpoint}")
	private String url;

	@Value("${amazon.s3.bucket-name}")
	private String bucketName;

	@Value("${amazon.s3.access-key}")
	private String accessKey;

	@Value("${amazon.s3.secret-key}")
	private String secretKey;

	@Value("${amazon.s3.enabled}")
	private boolean enabled;

	@PostConstruct
	private void init() {
		if (enabled) {
			BasicAWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
			this.s3client = AmazonS3ClientBuilder.standard().withRegion(Regions.EU_WEST_3).withCredentials(new AWSStaticCredentialsProvider(credentials)).build();
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
			image.setUrl(url + id);
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
		s3client.deleteObject(bucketName, id);

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
		s3client.putObject(new PutObjectRequest(bucketName, fileName, file).withCannedAcl(CannedAccessControlList.PublicRead));
	}

}