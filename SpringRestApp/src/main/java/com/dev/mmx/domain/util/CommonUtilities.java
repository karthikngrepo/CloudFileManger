package com.dev.mmx.domain.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.dev.mmx.domain.beans.input.FileMetaData;

public class CommonUtilities {
	
	private static Logger log = Logger.getLogger(CommonUtilities.class);
	
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

	public static FileMetaData getFileMetaInfo(Path path) throws IOException {
		FileMetaData metaData = new FileMetaData();
		BasicFileAttributes attr = Files.readAttributes(path, BasicFileAttributes.class);

		System.out.println("creationTime: " + attr.creationTime());
		System.out.println("lastAccessTime: " + attr.lastAccessTime());
		System.out.println("lastModifiedTime: " + attr.lastModifiedTime());

		metaData.set_lastModified(DAOUtil.sdf.format(new Date(attr.lastModifiedTime().toMillis())));
		metaData.set_accessed(DAOUtil.sdf.format(new Date(attr.lastModifiedTime().toMillis())));
		metaData.set_created(DAOUtil.sdf.format(new Date(attr.creationTime().toMillis())));
		metaData.set_contentType(Boolean.toString(Files.isHidden(path)));
		metaData.set_owner(Files.getOwner(path).getName());
		metaData.set_contentType(Files.probeContentType(path));
		if(path.getFileName().toString().contains(".")) {
			metaData.set_fileType(path.getFileName().toString().substring(path.getFileName().toString().lastIndexOf(".")));
		}
		metaData.set_fileSize((attr.size()/1024)+"KB");

		return metaData;
	}

	public static String getFileSize(Path path) {
		long fileSize = 0L;
		try {
			if(Files.exists(path)) {
				fileSize = (Files.size(path)/1024);
			} 
		}catch (IOException e) {
			e.printStackTrace();
			log.debug("Exception encountered while reading the file size !!!"+e.getMessage());
		}
		return fileSize+"KB";
	}
	
	public static String getMetaData(HttpServletRequest request, String string) {
		return request.getHeader(string);
	}
}