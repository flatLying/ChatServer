package hit.dreamer.chatserver.utils;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.util.Date;
import java.util.Locale;

public class TimeUtils {
	public static String getDatefromLocalDateTime(LocalDateTime localDateTime){
		int hour = localDateTime.getHour();
		int minute = localDateTime.getMinute();
		return hour+":"+minute;
	}
	public static String getTimestampfromLocalDateTime(LocalDateTime localDateTime){
		int dayOfMonth = localDateTime.getDayOfMonth();
		Month month = localDateTime.getMonth();
		return dayOfMonth+" "+month.getDisplayName(java.time.format.TextStyle.FULL, Locale.ENGLISH);
	}
	public static LocalDateTime getLocalDateTimefromDateAndTimestamp(String date,String timestamp){
		String[] hourMinute=date.split(":");
		int hour=Integer.parseInt(hourMinute[0]);
		int minute=Integer.parseInt(hourMinute[1]);
		String[] dayMonth=timestamp.split(" ");
		String month=dayMonth[1];
		int month_value = Month.valueOf(month.toUpperCase()).getValue();
		int day=Integer.parseInt(dayMonth[0]);
		return LocalDateTime.of(LocalDateTime.now().getYear(),month_value,day,hour,minute);
	}
	
}
