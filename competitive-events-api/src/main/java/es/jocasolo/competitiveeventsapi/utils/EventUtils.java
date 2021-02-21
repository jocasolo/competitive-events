package es.jocasolo.competitiveeventsapi.utils;

import java.util.Date;

public class EventUtils {
	
	private EventUtils() {}
	
	public static String getValue(String newValue, String actualValue) {
		return newValue != null ? newValue : actualValue;
	}
	
	public static Date getValue(Date newValue, Date actualValue) {
		return newValue != null ? newValue : actualValue;
	}
	
	public static Boolean getValue(Boolean newValue, Boolean actualValue) {
		return newValue != null ? newValue : actualValue;
	}
	
	public static Integer getValue(Integer newValue, Integer actualValue) {
		return newValue != null ? newValue : actualValue;
	}
	
}
