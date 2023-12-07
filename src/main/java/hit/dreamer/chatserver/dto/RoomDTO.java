package hit.dreamer.chatserver.dto;

import lombok.Data;

import java.util.List;

@Data
public class RoomDTO {
	private Long roomId;
	private String roomName;
	private String avatar;
	private Integer unreadCount;
	private  Integer index;
//	private String lastMessage;
	private List<SendUserDTO> users;
}
