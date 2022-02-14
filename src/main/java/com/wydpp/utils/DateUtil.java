package com.wydpp.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.Date;
import java.util.Locale;

/**    
 * @description:时间工具类，主要处理ISO 8601格式转换
 * @author: swwheihei
 * @date:   2020年5月8日 下午3:24:42     
 */
public class DateUtil {

	//private static final String yyyy_MM_dd_T_HH_mm_ss_SSSXXX = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";
	private static final String yyyy_MM_dd_T_HH_mm_ss_SSSXXX = "yyyy-MM-dd'T'HH:mm:ss";
    private static final String yyyy_MM_dd_HH_mm_ss = "yyyy-MM-dd HH:mm:ss";
    
	public static String yyyy_MM_dd_HH_mm_ssToISO8601(String formatTime) {

        SimpleDateFormat oldsdf = new SimpleDateFormat(yyyy_MM_dd_HH_mm_ss, Locale.getDefault());
        SimpleDateFormat newsdf = new SimpleDateFormat(yyyy_MM_dd_T_HH_mm_ss_SSSXXX, Locale.getDefault());
        try {
            return newsdf.format(oldsdf.parse(formatTime));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }
	
	public static String ISO8601Toyyyy_MM_dd_HH_mm_ss(String formatTime) {

        SimpleDateFormat oldsdf = new SimpleDateFormat(yyyy_MM_dd_T_HH_mm_ss_SSSXXX, Locale.getDefault());
        SimpleDateFormat newsdf = new SimpleDateFormat(yyyy_MM_dd_HH_mm_ss, Locale.getDefault());
        try {
            return newsdf.format(oldsdf.parse(formatTime));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static LocalDateTime toLocalDateTime(String timeStr){
        SimpleDateFormat oldsdf = new SimpleDateFormat(yyyy_MM_dd_T_HH_mm_ss_SSSXXX, Locale.getDefault());
        Date date = null;
        try {
            date = oldsdf.parse(timeStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public static boolean isToday(LocalDateTime localDateTime){
        return localDateTime != null && localDateTime.toLocalDate().equals(LocalDate.now(ZoneId.systemDefault()));
    }

	public static long yyyy_MM_dd_HH_mm_ssToTimestamp(String formatTime) {
		SimpleDateFormat format=new SimpleDateFormat(yyyy_MM_dd_HH_mm_ss);
		//设置要读取的时间字符串格式
		Date date;
		try {
			date = format.parse(formatTime);
			Long timestamp=date.getTime()/1000;
			//转换为Date类
			return timestamp;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}

    public static String getDateStr(){
        SimpleDateFormat oldsdf = new SimpleDateFormat(yyyy_MM_dd_HH_mm_ss, Locale.getDefault());
        return oldsdf.format(new Date());
    }
}
