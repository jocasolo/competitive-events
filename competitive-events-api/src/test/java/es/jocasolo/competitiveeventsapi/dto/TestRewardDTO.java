package es.jocasolo.competitiveeventsapi.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import es.jocasolo.competitiveeventsapi.dto.image.ImageDTO;
import es.jocasolo.competitiveeventsapi.dto.reward.RewardDTO;
import es.jocasolo.competitiveeventsapi.dto.user.UserLiteDTO;
import es.jocasolo.competitiveeventsapi.enums.score.ScoreSortType;

@RunWith(MockitoJUnitRunner.class)
class TestRewardDTO {

	private ImageDTO image = new ImageDTO();
	private RewardDTO reward = new RewardDTO();
	private UserLiteDTO winner = new UserLiteDTO();

	@BeforeEach
	void init() {

		MockitoAnnotations.openMocks(this);

		image.setId(1);
		
		reward.setId(1);
		reward.setTitle("title");
		reward.setWinner(winner);
		reward.setSortScore(ScoreSortType.ASC);
		reward.setRequiredPosition(1);
		reward.setImage(image);

	}

	@Test
	void testCommon() {

		assertEquals("title", reward.getTitle());
		assertEquals(winner, reward.getWinner());
		assertEquals(ScoreSortType.ASC, reward.getSortScore());
		assertEquals(image, reward.getImage());
		assertEquals("RewardDTO [id=1, title=title]", reward.toString());
		assertEquals(1, reward.getRequiredPosition());
	}

}
