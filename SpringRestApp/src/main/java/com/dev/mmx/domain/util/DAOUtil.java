package com.dev.mmx.domain.util;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.log4j.Logger;

import com.dev.mmx.domain.constant.CommonConstants;

public class DAOUtil {
	
	private static Logger log = Logger.getLogger(DAOUtil.class);
	
	public static SimpleDateFormat sdf = new SimpleDateFormat(CommonConstants.DATE_FORMAT_YYYY_MM_DD);
	
	public static Date getSQLDate(int get_numberOfDaysStats) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new java.util.Date());
		calendar.add(Calendar.DATE, 7);
		Date date = new Date(calendar.getTimeInMillis());
		
		return date;
	}
	
	public static boolean isValidDate(Object date) {
		boolean isDateParsable = false;
		try {
			sdf.format(date);
			isDateParsable = true;
		} catch (IllegalArgumentException e) {
			log.error("Date passed to the method is unparsable");
		}
		
		return isDateParsable;
	}
}
