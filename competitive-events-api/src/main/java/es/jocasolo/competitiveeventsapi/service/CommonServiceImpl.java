package es.jocasolo.competitiveeventsapi.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommonServiceImpl implements CommonService {

	@Autowired
	private DozerBeanMapper dozer;

	@Override
	public <T, S> T transform(S source, Class<T> destinationClass) {
		return dozer.map(source, destinationClass);
	}

	@Override
	public <T, S> List<T> transform(List<S> sources, Class<T> destinationClass) {
		List<T> res = new ArrayList<>();
		for (S source : sources)
			res.add(dozer.map(source, destinationClass));
		return res;
	}

	@Override
	public <T, S> Set<T> transform(Set<S> sources, Class<T> destinationClass) {
		Set<T> res = new HashSet<>();
		for (S source : sources)
			res.add(dozer.map(source, destinationClass));
		return res;
	}

}