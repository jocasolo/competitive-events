package es.jocasolo.competitiveeventsapi.service;

import java.util.Date;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import es.jocasolo.competitiveeventsapi.dao.EventUserDAO;
import es.jocasolo.competitiveeventsapi.dao.ScoreDAO;
import es.jocasolo.competitiveeventsapi.dto.score.ScoreDTO;
import es.jocasolo.competitiveeventsapi.dto.score.ScorePageDTO;
import es.jocasolo.competitiveeventsapi.dto.score.ScorePostDTO;
import es.jocasolo.competitiveeventsapi.dto.score.ScorePutDTO;
import es.jocasolo.competitiveeventsapi.enums.ImageType;
import es.jocasolo.competitiveeventsapi.enums.score.ScoreStatusType;
import es.jocasolo.competitiveeventsapi.exceptions.event.EventInvalidStatusException;
import es.jocasolo.competitiveeventsapi.exceptions.event.EventNotFoundException;
import es.jocasolo.competitiveeventsapi.exceptions.image.ImageUploadException;
import es.jocasolo.competitiveeventsapi.exceptions.score.ScoreNotFoundException;
import es.jocasolo.competitiveeventsapi.exceptions.score.ScoreWrongTypeException;
import es.jocasolo.competitiveeventsapi.exceptions.user.UserNotValidException;
import es.jocasolo.competitiveeventsapi.model.Event;
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
	public ScorePageDTO search(String eventId, PageRequest page) throws EventNotFoundException {
		
		Page<Score> scores = null;
		
		EventUser eventUser = eventUserDao.findOneByIds(eventId, authentication.getUser().getId());
		if(eventUser == null)
			throw new EventNotFoundException();

		scores = scoreDao.search(eventUser.getEvent(), page);
				
		ScorePageDTO dto = new ScorePageDTO();
		dto.setScores(commonService.transform(scores.getContent(), ScoreDTO.class));
		dto.setTotal(scores.getTotalElements());
		dto.setHasNext(scores.hasNext());
		dto.setHasPrevious(scores.hasPrevious());
		dto.setPages(scores.getTotalPages());
		
		return dto;
	}

	@Override
	public ScoreDTO create(ScorePostDTO scoreDto) 
			throws EventNotFoundException, UserNotValidException, EventInvalidStatusException, ScoreWrongTypeException {
		
		User user = authentication.getUser();
		
		EventUser eventUser = eventUserDao.findOneByIds(scoreDto.getEventId(), user.getId());
		if(eventUser == null)
			throw new EventNotFoundException();
		
		if(!eventUser.getEvent().isInDateRange())
			throw new EventInvalidStatusException();
		
		final Event event = eventUser.getEvent();
		if(!validScore(event, scoreDto.getValue())) {
			throw new ScoreWrongTypeException(event.getScoreType());
		}
		
		Score score = new Score();
		score.setUser(user);
		score.setEvent(event);
		score.setStatus(ScoreStatusType.VALID);
		score.setValue(scoreDto.getValue());
		score.setDate(new Date());
		
		return commonService.transform(scoreDao.save(score), ScoreDTO.class);
	}

	private boolean validScore(Event event, String value) {
		boolean result = false;
		
		switch (event.getScoreType()) {
			case NUMERIC:
			case TIME:
				result = NumberUtils.isDigits(value);
				break;
			case DECIMAL:
				result = NumberUtils.isCreatable(value);
		}
		
		return result;
	}

	@Override
	public void update(Integer id, ScorePutDTO scoreDto) throws ScoreNotFoundException, UserNotValidException, EventNotFoundException, EventInvalidStatusException {
		
		Score score = scoreDao.findOne(id);
		if(score == null)
			throw new ScoreNotFoundException();
		
		User user = authentication.getUser();
		if(!score.getUser().equals(user))
			throw new UserNotValidException();
		
		EventUser eventUser = eventUserDao.findOneByIds(score.getEvent().getId(), user.getId());
		if(eventUser == null)
			throw new EventNotFoundException();
		
		if(!eventUser.getEvent().isInDateRange())
			throw new EventInvalidStatusException();
		
		if(eventUser.isOwner())
			score.setStatus(scoreDto.getStatus());
		
		score.setValue(scoreDto.getValue());
		score.setDate(new Date());
		
		scoreDao.save(score);
	}

	@Override
	public void delete(Integer id) throws EventNotFoundException, UserNotValidException, ScoreNotFoundException, EventInvalidStatusException {
		
		Score score = scoreDao.findOne(id);
		if(score == null)
			throw new ScoreNotFoundException();
		
		if(!score.getEvent().isInDateRange())
			throw new EventInvalidStatusException();
		
		User user = authentication.getUser();
		if(!score.getUser().equals(user))
			throw new UserNotValidException();
		
		scoreDao.delete(score);
		
	}

	@Override
	public ScoreDTO updateImage(Integer id, MultipartFile multipart) throws ImageUploadException, ScoreNotFoundException, UserNotValidException, EventInvalidStatusException {
		
		Score score = scoreDao.findOne(id);
		if(score == null)
			throw new ScoreNotFoundException();
		
		if(!score.getEvent().isInDateRange())
			throw new EventInvalidStatusException();
		
		User user = authentication.getUser();
		if(!score.getUser().equals(user))
			throw new UserNotValidException();
		
		Image image = imageService.upload(multipart, ImageType.SCORE);
		score.setImage(image);
		
		return commonService.transform(scoreDao.save(score), ScoreDTO.class);
	}

}
