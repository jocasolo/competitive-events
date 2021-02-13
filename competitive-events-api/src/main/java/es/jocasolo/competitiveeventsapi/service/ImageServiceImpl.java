package es.jocasolo.competitiveeventsapi.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import javax.annotation.PostConstruct;

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
import es.jocasolo.competitiveeventsapi.dto.image.ImageDTO;
import es.jocasolo.competitiveeventsapi.enums.ImageType;
import es.jocasolo.competitiveeventsapi.model.Image;
import es.jocasolo.competitiveeventsapi.model.user.User;

@Service
public class ImageServiceImpl implements ImageService {
	
	private static final Logger log = LoggerFactory.getLogger(ImageServiceImpl.class);
	
	@Autowired
	private ImageDAO imageDao;
	
	@Autowired
	private CommonService commonService;

	private AmazonS3 s3client;

	@Value("${amazon.s3.endpoint}")
	private String url;

	@Value("${amazon.s3.bucket-name}")
	private String bucketName;

	@Value("${amazon.s3.access-key}")
	private String accessKey;

	@Value("${amazon.s3.secret-key}")
	private String secretKey;

	@PostConstruct
	private void init() {

		BasicAWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);

		this.s3client = AmazonS3ClientBuilder.standard().withRegion(Regions.EU_WEST_3)
				.withCredentials(new AWSStaticCredentialsProvider(credentials)).build();
	}

	@Override
	public ImageDTO upload(MultipartFile multipart, ImageType type) {
		
		ImageDTO imageDto = new ImageDTO();
		
		try {
			final File file = convertMultiPartToFile(multipart);
			final String fileName = generateFileName(multipart);
			final String folder = type.name().toLowerCase();
			final String id = folder + "/" + fileName;
			
			//uploadFileToS3bucket(id, file);
			
			final Image image = new Image();
			image.setUrl(url + id);
			image.setType(type);
			image.setFolder(folder);
			image.setName(fileName);
			image.setStorageId(id);
			// TODO owner
			
			// Save
			return commonService.transform(imageDao.save(image), ImageDTO.class);
			
		} catch (IOException e) {
			log.error("Error uploading image.");
			// TODO custom exception
		}
		
		return imageDto;
	}

	@Override
	public void delete(String folder, String name) {
		s3client.deleteObject(bucketName, folder + "/" + name);
	}
	
	private File convertMultiPartToFile(MultipartFile file) throws IOException {
	    File convFile = new File(file.getOriginalFilename());
	    FileOutputStream fos = new FileOutputStream(convFile);
	    fos.write(file.getBytes());
	    fos.close();
	    return convFile;
	}
	
	private String generateFileName(MultipartFile multiPart) {
	    return new Date().getTime() + "-" + multiPart.getOriginalFilename().replace(" ", "_");
	}
	
	private void uploadFileToS3bucket(String fileName, File file) {
	    s3client.putObject(new PutObjectRequest(bucketName, fileName, file)
	            .withCannedAcl(CannedAccessControlList.PublicRead));
	}
	
}