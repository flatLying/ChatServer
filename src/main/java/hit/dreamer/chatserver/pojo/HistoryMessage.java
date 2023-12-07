package hit.dreamer.chatserver.pojo;

import hit.dreamer.chatserver.netty.message.ChatMessage;
import hit.dreamer.chatserver.utils.TimeUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HistoryMessage {
	public HistoryMessage(ChatMessage chatMessage){
		this.setId(chatMessage.get_id());
		this.setIndexId(chatMessage.getIndexId());
		this.setContent(chatMessage.getContent());
		this.setSenderId(chatMessage.getSenderId());
		this.setUsername(chatMessage.getUsername());
		this.setAvatar(chatMessage.getAvatar());
		this.setSystem(chatMessage.getSystem());
		this.setSaved(chatMessage.getSaved());
		this.setDistributed(chatMessage.getDistributed());
		this.setSeen(chatMessage.getSeen());
		this.setDeleted(chatMessage.getDeleted());
		this.setFailure(chatMessage.getFailure());
		this.setDisableActions(chatMessage.getDisableActions());
		this.setDisableReactions(chatMessage.getDisableReactions());
		this.setFiles(chatMessage.getFiles());
		this.setReactions(chatMessage.getReactions());
		this.setReplyMessage(chatMessage.getReplyMessage());
		this.setUsersTag(chatMessage.getUsersTag());
		this.setRoomId(chatMessage.getRoomId());
		this.setSendTime(TimeUtils.getLocalDateTimefromDateAndTimestamp(chatMessage.getDate(), chatMessage.getTimestamp()));
	}
	private Long id;
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
	private Long roomId;
	/**
	 * 消息时间
	 */
	private LocalDateTime sendTime;


}
