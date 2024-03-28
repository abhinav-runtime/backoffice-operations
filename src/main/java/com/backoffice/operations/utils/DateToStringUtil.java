package com.backoffice.operations.utils;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

import org.springframework.stereotype.Component;

@Component
public class DateToStringUtil {
	
	
	public static String convertDateToString(Date date) {
		SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy h:mm:ss a");
		String createdDateTime = formatter.format(date);
		return createdDateTime;
	}
	
	public static String convertLocalDateToString(LocalDateTime date) {
		SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy h:mm:ss a");
		String createdDateTime = formatter.format(date);
		return createdDateTime;
	}
}
