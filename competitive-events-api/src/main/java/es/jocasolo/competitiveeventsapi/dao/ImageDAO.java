package es.jocasolo.competitiveeventsapi.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import es.jocasolo.competitiveeventsapi.model.Image;

@Repository
public interface ImageDAO extends CrudRepository<Image, String> {
	
	
}
