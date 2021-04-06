package es.jocasolo.competitiveeventsapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import es.jocasolo.competitiveeventsapi.dao.EventUserDAO;
import es.jocasolo.competitiveeventsapi.dao.PunishmentDAO;
import es.jocasolo.competitiveeventsapi.dto.punishment.PunishmentDTO;
import es.jocasolo.competitiveeventsapi.dto.punishment.PunishmentPostDTO;
import es.jocasolo.competitiveeventsapi.dto.punishment.PunishmentPutDTO;
import es.jocasolo.competitiveeventsapi.enums.ImageType;
import es.jocasolo.competitiveeventsapi.exceptions.event.EventNotFoundException;
import es.jocasolo.competitiveeventsapi.exceptions.image.ImageUploadException;
import es.jocasolo.competitiveeventsapi.exceptions.punishment.PunishmentNotFoundException;
import es.jocasolo.competitiveeventsapi.exceptions.user.UserNotValidException;
import es.jocasolo.competitiveeventsapi.model.EventUser;
import es.jocasolo.competitiveeventsapi.model.Image;
import es.jocasolo.competitiveeventsapi.model.Punishment;
import es.jocasolo.competitiveeventsapi.model.User;
import es.jocasolo.competitiveeventsapi.utils.EventUtils;
import es.jocasolo.competitiveeventsapi.utils.security.AuthenticationFacade;

@Service
public class PunishmentServiceImpl implements PunishmentService {
	
	@Autowired
	private PunishmentDAO punishmentDao;
	
	@Autowired
	private EventUserDAO eventUserDao;

	@Autowired
	private CommonService commonService;

	@Autowired
	private ImageService imageService;
	
	@Autowired
	private AuthenticationFacade authentication;
	
	// TODO winner

	@Override
	public Punishment findOne(Integer id) throws PunishmentNotFoundException {
		final Punishment punishment = punishmentDao.findOne(id);
		if (punishment == null)
			throw new PunishmentNotFoundException();
		
		return punishment;
	}

	@Override
	public PunishmentDTO create(PunishmentPostDTO punishmentDto) throws EventNotFoundException, UserNotValidException {
		
		User user = authentication.getUser();
		
		EventUser eventUser = eventUserDao.findOneByIds(punishmentDto.getEventId(), user.getId());
		if(eventUser == null)
			throw new EventNotFoundException();
		
		if(!eventUser.isOwner())
			throw new UserNotValidException();
		
		Punishment punishment = new Punishment();
		punishment.setTitle(punishmentDto.getTitle());
		punishment.setDescription(punishmentDto.getDescription());
		punishment.setEvent(eventUser.getEvent());
		punishment.setRequiredPosition(punishmentDto.getRequiredPosition());
		
		return commonService.transform(punishmentDao.save(punishment), PunishmentDTO.class);
	}

	@Override
	public void update(Integer id, PunishmentPutDTO punishmentDto) throws PunishmentNotFoundException, UserNotValidException {
		
		User user = authentication.getUser();
		Punishment punishment = punishmentDao.findOne(id);
		if(punishment == null)
			throw new PunishmentNotFoundException();
		
		EventUser eventUser = eventUserDao.findOneByIds(punishment.getEvent().getId(), user.getId());
		if(!eventUser.isOwner())
			throw new UserNotValidException();
		
		punishment.setDescription(EventUtils.getValue(punishmentDto.getDescription(), punishment.getDescription()));
		punishment.setTitle(EventUtils.getValue(punishmentDto.getTitle(), punishment.getTitle()));
		punishment.setRequiredPosition(EventUtils.getValue(punishmentDto.getRequiredPosition(), punishment.getRequiredPosition()));
		
		punishmentDao.save(punishment);
		
	}

	@Override
	public void delete(Integer id) throws EventNotFoundException, UserNotValidException, PunishmentNotFoundException {
		
		Punishment punishment = punishmentDao.findOne(id);
		if(punishment == null)
			throw new PunishmentNotFoundException();
		
		User user = authentication.getUser();
		EventUser eventUser = eventUserDao.findOneByIds(punishment.getEvent().getId(), user.getId());
		if(!eventUser.isOwner())
			throw new UserNotValidException();
		
		punishmentDao.delete(punishment);
		
	}

	@Override
	public Object updateImage(Integer id, MultipartFile multipart) throws ImageUploadException, PunishmentNotFoundException, UserNotValidException {
		
		Punishment punishment = punishmentDao.findOne(id);
		if(punishment == null)
			throw new PunishmentNotFoundException();
		
		User user = authentication.getUser();
		EventUser eventUser = eventUserDao.findOneByIds(punishment.getEvent().getId(), user.getId());
		if(!eventUser.isOwner())
			throw new UserNotValidException();
		
		Image image = imageService.upload(multipart, ImageType.REWARD);
		punishment.setImage(image);
		
		return commonService.transform(punishmentDao.save(punishment), PunishmentDTO.class);
	}

}
