package es.jocasolo.competitiveeventsapi.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import es.jocasolo.competitiveeventsapi.enums.ImageType;

@Component
public class ImageTypeConverter implements Converter<String, ImageType> {

    @Override
    public ImageType convert(String value) {
    	try {
    		return ImageType.valueOf(value.toUpperCase());
    	} catch (Exception e) {
    		return ImageType.OTHER;
    	}
    }
}