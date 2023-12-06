package hit.dreamer.chatserver.pojo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class HistoryMessage {
	private Long _id;
	private Long indexId;
	private String content;
	private Long senderId;
	private String username;
	private String avatar;
	private Boolean system;
	private Boolean saved;
	private Boolean distributed;
	private Boolean seen;
	private Boolean deleted;
	private Boolean failure;
	private Boolean disableActions;
	private Boolean disableReactions;
	private String files;
	private String reactions;
	private String replyMessage;
	private String usersTag;
	/**
	 * 消息时间
	 */
	private LocalDateTime sendTime;
}
