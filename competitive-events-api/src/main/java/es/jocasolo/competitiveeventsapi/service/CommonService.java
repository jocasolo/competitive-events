package es.jocasolo.competitiveeventsapi.service;

import java.util.List;
import java.util.Set;

public interface CommonService {

	/**
	 * Transforma un objeto de un tipo, en el tipo indicado por parámetro.
	 * @param source
	 * @param destinationClass
	 * @return
	 */
	<T, S> T transform(S source, Class<T> destinationClass);

	/**
	 * Transforma una lista de objetos de un tipo, en otra lista de objetos del
	 * tipo indicado por parámetro.
	 * @param sources
	 * @param destinationClass
	 * @return
	 */
	<T, S> List<T> transform(List<S> sources, Class<T> destinationClass);
	
	/**
	 * Transforma un set de objetos de un tipo, en otra lista de objetos del
	 * tipo indicado por parámetro.
	 * @param sources
	 * @param destinationClass
	 * @return
	 */
	<T, S> Set<T> transform(Set<S> sources, Class<T> destinationClass);

}