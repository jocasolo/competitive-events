package es.jocasolo.competitiveeventsapi.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import es.jocasolo.competitiveeventsapi.dao.EventUserDAO;
import es.jocasolo.competitiveeventsapi.dao.ScoreDAO;
import es.jocasolo.competitiveeventsapi.dto.score.ScoreDTO;
import es.jocasolo.competitiveeventsapi.dto.score.ScorePostDTO;
import es.jocasolo.competitiveeventsapi.dto.score.ScorePutDTO;
import es.jocasolo.competitiveeventsapi.enums.ImageType;
import es.jocasolo.competitiveeventsapi.enums.score.ScoreStatusType;
import es.jocasolo.competitiveeventsapi.exceptions.event.EventNotFoundException;
import es.jocasolo.competitiveeventsapi.exceptions.image.ImageUploadException;
import es.jocasolo.competitiveeventsapi.exceptions.score.ScoreNotFoundException;
import es.jocasolo.competitiveeventsapi.exceptions.user.UserNotValidException;
import es.jocasolo.competitiveeventsapi.model.EventUser;
import es.jocasolo.competitiveeventsapi.model.Image;
import es.jocasolo.competitiveeventsapi.model.Score;
import es.jocasolo.competitiveeventsapi.model.User;
import es.jocasolo.competitiveeventsapi.utils.security.AuthenticationFacade;

@Service
public class ScoreServiceImpl implements ScoreService {
	
	@Autowired
	private ScoreDAO scoreDao;
	
	@Autowired
	private EventUserDAO eventUserDao;

	@Autowired
	private CommonService commonService;

	@Autowired
	private ImageService imageService;
	
	@Autowired
	private AuthenticationFacade authentication;
	
	@Override
	public Score findOne(Integer id) throws ScoreNotFoundException {
		final Score score = scoreDao.findOne(id);
		if (score == null)
			throw new ScoreNotFoundException();
		
		return score;
	}

	@Override
	public ScoreDTO create(ScorePostDTO scoreDto) throws EventNotFoundException, UserNotValidException {
		
		User user = authentication.getUser();
		
		EventUser eventUser = eventUserDao.findOneByIds(scoreDto.getEventId(), user.getId());
		if(eventUser == null)
			throw new EventNotFoundException();
		
		if(!eventUser.isOwner() && !user.isSuperuser())
			throw new UserNotValidException();
		
		Score score = new Score();
		score.setUser(user);
		score.setEvent(eventUser.getEvent());
		score.setStatus(ScoreStatusType.VALID);
		score.setValue(scoreDto.getValue());
		score.setDate(new Date());
		
		return commonService.transform(scoreDao.save(score), ScoreDTO.class);
	}

	@Override
	public void update(Integer id, ScorePutDTO scoreDto) throws ScoreNotFoundException {
		
		User user = authentication.getUser();
		Score score = scoreDao.findOne(id);
		if(score == null)
			throw new ScoreNotFoundException();
		
		EventUser eventUser = eventUserDao.findOneByIds(score.getEvent().getId(), user.getId());
		if(eventUser.isAdmin() || eventUser.isOwner() || user.isSuperuser())
			score.setStatus(scoreDto.getStatus());
		
		score.setValue(scoreDto.getValue());
		score.setDate(new Date());
		
		scoreDao.save(score);
	}

	@Override
	public void delete(Integer id) throws EventNotFoundException, UserNotValidException, ScoreNotFoundException {
		
		Score score = scoreDao.findOne(id);
		if(score == null)
			throw new ScoreNotFoundException();
		
		User user = authentication.getUser();
		if(!score.getUser().equals(user) && !user.isSuperuser())
			throw new UserNotValidException();
		
		scoreDao.delete(score);
		
	}

	@Override
	public ScoreDTO updateImage(Integer id, MultipartFile multipart) throws ImageUploadException, ScoreNotFoundException, UserNotValidException {
		
		Score score = scoreDao.findOne(id);
		if(score == null)
			throw new ScoreNotFoundException();
		
		User user = authentication.getUser();
		if(!score.getUser().equals(user) && !user.isSuperuser())
			throw new UserNotValidException();
		
		Image image = imageService.upload(multipart, ImageType.REWARD);
		score.setImage(image);
		
		return commonService.transform(scoreDao.save(score), ScoreDTO.class);
	}

}
