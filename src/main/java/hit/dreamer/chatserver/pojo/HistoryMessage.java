package hit.dreamer.chatserver.pojo;

import lombok.Data;

@Data
public class HistoryMessage {
	/**
	 * 主键
	 */
	private Long id;
	/**
	 * 发送者id
	 */
	private Long senderId;
	/**
	 * 群聊id
	 */
	private Long roomId;
	/**
	 * 消息
	 */
	private String message;
}
