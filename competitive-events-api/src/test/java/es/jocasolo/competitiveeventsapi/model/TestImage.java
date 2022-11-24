package es.jocasolo.competitiveeventsapi.model;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import es.jocasolo.competitiveeventsapi.enums.ImageType;

@RunWith(MockitoJUnitRunner.class)
class TestImage {

	private Image image = new Image();
	private Event event = new Event();
	private User user = new User();

	@BeforeEach
	void init() {

		MockitoAnnotations.openMocks(this);

		image.setId(1);
		image.setEvent(event);
		image.setEvents(Set.of(event));
		image.setFolder("folder");
		image.setName("name");
		image.setOwner(user);
		image.setStorageId("storage");
		image.setType(ImageType.OTHER);
		image.setUrl("url");

	}

	@Test
	void testCommon() {
		assertEquals(ImageType.OTHER, image.getType());
		assertEquals("folder", image.getFolder());
		assertEquals("name", image.getName());
		assertNotNull(image.getEvents());
		assertEquals(event, image.getEvent());
		assertEquals("Image [id=1]", image.toString());
	}

}
