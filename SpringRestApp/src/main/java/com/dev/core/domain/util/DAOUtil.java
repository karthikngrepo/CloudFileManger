package com.dev.core.domain.util;

import java.sql.Date;
import java.util.Calendar;

public class DAOUtil {
	public static Date getSQLDate(int get_numberOfDaysStats) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new java.util.Date());
		calendar.add(Calendar.DATE, 7);
		Date date = new Date(calendar.getTimeInMillis());
		
		return date;
	}
}
