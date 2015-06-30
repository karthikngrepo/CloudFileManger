package com.dev.core.domain.util;

import java.util.Calendar;

public class CommonUtilities {
	public static String convertArrayToCSV(String[] array) {
		System.out.println("Input Class Names Array:"+array);
		String classNames = null;
		for(String className : array) {
			classNames = className+",";
		}
		if(classNames != null) {
			classNames = classNames.substring(0,classNames.length()-1);
		}
		System.out.println("Class Names CSV:"+classNames);
		return classNames;
	}
	
	public static long getRecentPreviousHour() {
		long hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
		long hoursToAdd = (hour%4);
		System.out.println("Most nearest hour :"+hour +"-"+ hoursToAdd);
		return hour - hoursToAdd;
	}
}