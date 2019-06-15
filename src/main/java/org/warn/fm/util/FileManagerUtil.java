package org.warn.fm.util;

import java.text.SimpleDateFormat;

import com.fasterxml.jackson.databind.ObjectMapper;

public class FileManagerUtil {
	
	public static final SimpleDateFormat fullTimestampSDF = new SimpleDateFormat( GlobalConstants.FULL_TS_FORMAT );
	public static final ObjectMapper mapper = new ObjectMapper();
	
	private static final long KB = 1024;
	private static final long MB = 1048576;
	private static final long GB = 1073741824;
	
	public static String printFileSizeUserFriendly( long fileSize ) {
		if( fileSize < KB ) {
			return fileSize + "B";
		} else if( fileSize >= KB && fileSize < MB ) {
			return fileSize / KB + "KB";
		} else if( fileSize >= MB && fileSize < GB ) {
			return fileSize / MB + "MB";
		} else {
			return fileSize / GB + "GB";
		}
	}

}
