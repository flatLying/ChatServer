package hit.dreamer.chatserver.service;

import hit.dreamer.chatserver.dto.Result;

public interface RoomService {
	public Result getRooms();
	
	Result getMessageByRoomId(Long roomId);
}
