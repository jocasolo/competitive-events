package es.jocasolo.competitiveeventsapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import es.jocasolo.competitiveeventsapi.dao.EventUserDAO;
import es.jocasolo.competitiveeventsapi.dao.RewardDAO;
import es.jocasolo.competitiveeventsapi.dto.reward.RewardDTO;
import es.jocasolo.competitiveeventsapi.dto.reward.RewardPostDTO;
import es.jocasolo.competitiveeventsapi.dto.reward.RewardPutDTO;
import es.jocasolo.competitiveeventsapi.enums.ImageType;
import es.jocasolo.competitiveeventsapi.enums.score.ScoreSortType;
import es.jocasolo.competitiveeventsapi.exceptions.event.EventNotFoundException;
import es.jocasolo.competitiveeventsapi.exceptions.image.ImageUploadException;
import es.jocasolo.competitiveeventsapi.exceptions.reward.RewardNotFoundException;
import es.jocasolo.competitiveeventsapi.exceptions.user.UserNotValidException;
import es.jocasolo.competitiveeventsapi.model.EventUser;
import es.jocasolo.competitiveeventsapi.model.Image;
import es.jocasolo.competitiveeventsapi.model.Reward;
import es.jocasolo.competitiveeventsapi.model.User;
import es.jocasolo.competitiveeventsapi.utils.EventUtils;
import es.jocasolo.competitiveeventsapi.utils.security.AuthenticationFacade;

@Service
public class RewardServiceImpl implements RewardService {
	
	@Autowired
	private RewardDAO rewardDao;
	
	@Autowired
	private EventUserDAO eventUserDao;

	@Autowired
	private CommonService commonService;

	@Autowired
	private ImageService imageService;
	
	@Autowired
	private AuthenticationFacade authentication;
	
	@Override
	public Reward findOne(Integer id) throws RewardNotFoundException {
		final Reward reward = rewardDao.findOne(id);
		if (reward == null)
			throw new RewardNotFoundException();
		
		return reward;
	}

	@Override
	public RewardDTO create(RewardPostDTO rewardDto) throws EventNotFoundException, UserNotValidException {
		
		User user = authentication.getUser();
		
		EventUser eventUser = eventUserDao.findOneByIds(rewardDto.getEventId(), user.getId());
		if(eventUser == null)
			throw new EventNotFoundException();
		
		if(!eventUser.isOwner())
			throw new UserNotValidException();
		
		Reward reward = new Reward();
		reward.setTitle(rewardDto.getTitle());
		reward.setDescription(rewardDto.getDescription());
		reward.setEvent(eventUser.getEvent());
		reward.setRequiredPosition(rewardDto.getRequiredPosition());
		reward.setSortScore(ScoreSortType.getValue(rewardDto.getSortScore(), ScoreSortType.ASC));
		
		return commonService.transform(rewardDao.save(reward), RewardDTO.class);
	}

	@Override
	public void update(Integer id, RewardPutDTO rewardDto) throws RewardNotFoundException, UserNotValidException {
		
		User user = authentication.getUser();
		Reward reward = rewardDao.findOne(id);
		if(reward == null)
			throw new RewardNotFoundException();
		
		EventUser eventUser = eventUserDao.findOneByIds(reward.getEvent().getId(), user.getId());
		if(!eventUser.isOwner())
			throw new UserNotValidException();
		
		reward.setDescription(EventUtils.getValue(rewardDto.getDescription(), reward.getDescription()));
		reward.setTitle(EventUtils.getValue(rewardDto.getTitle(), reward.getTitle()));
		reward.setRequiredPosition(EventUtils.getValue(rewardDto.getRequiredPosition(), reward.getRequiredPosition()));
		reward.setSortScore(ScoreSortType.getValue(rewardDto.getSortScore(), reward.getSortScore()));
		
		rewardDao.save(reward);
		
	}

	@Override
	public void delete(Integer id) throws EventNotFoundException, UserNotValidException, RewardNotFoundException {
		
		Reward reward = rewardDao.findOne(id);
		if(reward == null)
			throw new RewardNotFoundException();
		
		User user = authentication.getUser();
		EventUser eventUser = eventUserDao.findOneByIds(reward.getEvent().getId(), user.getId());
		if(!eventUser.isOwner())
			throw new UserNotValidException();
		
		rewardDao.delete(reward);
		
	}

	@Override
	public RewardDTO updateImage(Integer id, MultipartFile multipart) throws ImageUploadException, RewardNotFoundException, UserNotValidException {
		
		Reward reward = rewardDao.findOne(id);
		if(reward == null)
			throw new RewardNotFoundException();
		
		User user = authentication.getUser();
		EventUser eventUser = eventUserDao.findOneByIds(reward.getEvent().getId(), user.getId());
		if(!eventUser.isOwner())
			throw new UserNotValidException();
		
		Image image = imageService.upload(multipart, ImageType.REWARD);
		reward.setImage(image);
		
		return commonService.transform(rewardDao.save(reward), RewardDTO.class);
	}

}
