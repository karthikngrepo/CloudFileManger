package com.dev.mmx.domain.util;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.dev.mmx.domain.constant.CommonConstants;

public class DAOUtil {
	
	public static SimpleDateFormat sdf = new SimpleDateFormat(CommonConstants.DATE_FORMAT_YYYY_MM_DD);
	
	public static Date getSQLDate(int get_numberOfDaysStats) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new java.util.Date());
		calendar.add(Calendar.DATE, 7);
		Date date = new Date(calendar.getTimeInMillis());
		
		return date;
	}
}
