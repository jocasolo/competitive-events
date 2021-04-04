package es.jocasolo.competitiveeventsapi.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.jocasolo.competitiveeventsapi.dao.CommentDAO;
import es.jocasolo.competitiveeventsapi.dao.EventUserDAO;
import es.jocasolo.competitiveeventsapi.dto.comment.CommentDTO;
import es.jocasolo.competitiveeventsapi.dto.comment.CommentPostDTO;
import es.jocasolo.competitiveeventsapi.dto.comment.CommentPutDTO;
import es.jocasolo.competitiveeventsapi.exceptions.comment.CommentNotFoundException;
import es.jocasolo.competitiveeventsapi.exceptions.event.EventNotFoundException;
import es.jocasolo.competitiveeventsapi.exceptions.user.UserNotValidException;
import es.jocasolo.competitiveeventsapi.model.Comment;
import es.jocasolo.competitiveeventsapi.model.EventUser;
import es.jocasolo.competitiveeventsapi.model.User;
import es.jocasolo.competitiveeventsapi.utils.security.AuthenticationFacade;

@Service
public class CommentServiceImpl implements CommentService {
	
	@Autowired
	private CommentDAO commentDao;
	
	@Autowired
	private EventUserDAO eventUserDao;

	@Autowired
	private CommonService commonService;

	@Autowired
	private AuthenticationFacade authentication;
	
	@Override
	public Comment findOne(Integer id) throws CommentNotFoundException {
		final Comment comment = commentDao.findOne(id);
		if (comment == null)
			throw new CommentNotFoundException();
		
		return comment;
	}

	@Override
	public CommentDTO create(CommentPostDTO commentDto) throws EventNotFoundException, UserNotValidException {
		
		User user = authentication.getUser();
		
		EventUser eventUser = eventUserDao.findOneByIds(commentDto.getEventId(), user.getId());
		if(eventUser == null)
			throw new EventNotFoundException();
		
		if(!eventUser.isOwner() && !user.isSuperuser())
			throw new UserNotValidException();
		
		Comment comment = new Comment();
		comment.setUser(user);
		comment.setEvent(eventUser.getEvent());
		comment.setText(commentDto.getText());
		comment.setDate(new Date());
		
		return commonService.transform(commentDao.save(comment), CommentDTO.class);
	}

	@Override
	public void update(Integer id, CommentPutDTO commentDto) throws CommentNotFoundException, UserNotValidException {
		
		User user = authentication.getUser();
		Comment comment = commentDao.findOne(id);
		
		if(comment == null)
			throw new CommentNotFoundException();
		
		if(!comment.getUser().equals(user) && !user.isSuperuser())
			throw new UserNotValidException();
		
		comment.setText(commentDto.getText());
		comment.setDate(new Date());
		
		commentDao.save(comment);
	}

	@Override
	public void delete(Integer id) throws EventNotFoundException, UserNotValidException, CommentNotFoundException {
		
		Comment comment = commentDao.findOne(id);
		if(comment == null)
			throw new CommentNotFoundException();
		
		User user = authentication.getUser();
		if(!comment.getUser().equals(user) && !user.isSuperuser())
			throw new UserNotValidException();
		
		commentDao.delete(comment);
		
	}

}
