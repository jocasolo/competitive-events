package es.jocasolo.competitiveeventsapi.utils;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;

public class EventUtils {
	
	private EventUtils() {}
	
	public static String getValue(String newValue, String actualValue) {
		return StringUtils.isNotEmpty(newValue) ? newValue : actualValue;
	}
	
	public static Date getValue(Date newValue, Date actualValue) {
		return newValue != null ? newValue : actualValue;
	}
	
	public static Boolean getValue(Boolean newValue, Boolean actualValue) {
		return newValue != null ? newValue : actualValue;
	}

}
