package hit.dreamer.chatserver.dto;

import cn.hutool.core.date.DateUtil;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.util.Date;
import java.util.Locale;

@Data
public class SendUserDTO {
	private Long _id;
	private String username;
	private String avatar;
	private Status status=new Status();
	@Data
	private class Status{
		private String state;
		private String lastChanged;
	}
	public void setStatus(boolean isLogin,LocalDateTime updateTime){
		String state = isLogin ? "online" : "offline";
		int hour = updateTime.getHour();
		int minute = updateTime.getMinute();
		int dayOfMonth = updateTime.getDayOfMonth();
		Month month = updateTime.getMonth();
		ZoneId zoneId = ZoneId.systemDefault();
		Date today= Date.from(LocalDateTime.now().atZone(zoneId).toInstant());
		Date lastLogin=Date.from(updateTime.atZone(zoneId).toInstant());
		String date= DateUtil.betweenDay(today,lastLogin,false)==0?"today":dayOfMonth+month.getDisplayName(
				java.time.format.TextStyle.FULL,Locale.CHINA);
		this.status.setState(state);
		this.status.setLastChanged(date+", "+hour+":"+minute);
	}
}
