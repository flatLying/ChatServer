package hit.dreamer.chatserver.dto;

import hit.dreamer.chatserver.netty.message.ChatMessage;
import lombok.Data;

import java.util.List;

@Data
public class RoomDTO {
	private Long roomId;
	private String roomName;
	private String avatar;
	private Long unreadCount;
	private  Integer index;
	private ChatMessage lastMessage;
	private List<SendUserDTO> users;
}
