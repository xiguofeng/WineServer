
package com.xgf.wineserver.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtils {

    public static final String FORMAT_PATTERN_DATE = "yyyy-MM-dd HH:mm:ss";

    public static long dateToLong(Date date) {
        
        return date.getTime() / 1000; // 得到秒数，Date类型的getTime()返回毫秒数 
    }

    /**
     * 将Unix时间戳改成正常时间
     * 
     * @param timestampString Unix时间戳
     * @param pattern 要显示的日期的格式，例如:yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String TimeStamp2Date(String timestampString, String pattern) {
        if (timestampString != null && !"".equals(timestampString)) {
            Long timestamp = Long.parseLong(timestampString);
            String date = new java.text.SimpleDateFormat(pattern).format(new java.util.Date(
                    timestamp));
            return date;
        }
        return "";
    }
    
    public static String getTodayCommonPattern() {
		String str = TimeUtils.TimeStamp2Date(
				String.valueOf(System.currentTimeMillis()),
				TimeUtils.FORMAT_PATTERN_DATE);
		return str;
	}
    
    public static long dateToLong(String date, String pattern) {
		long time;
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		Date dt2 = null;
		try {
			dt2 = sdf.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		time = dt2.getTime() / 1000;
		return time; // 得到秒数，Date类型的getTime()返回毫秒数
	}
    
}
