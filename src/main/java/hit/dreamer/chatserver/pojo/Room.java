package hit.dreamer.chatserver.pojo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Room {
	private static final long serialVersionUID = 1L;
	
	/**
	 * 主键
	 */
	private Long id;
	
	/**
	 * 群聊名称
	 */
	private String roomName;
	
	/**
	 * 用户头像
	 */
	private String avatar = "";
	
	/**
	 * 创建时间
	 */
	private LocalDateTime createTime;
	
	/**
	 * 更新时间
	 */
	private LocalDateTime updateTime;
}
