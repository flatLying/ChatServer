package hit.dreamer.chatserver.controller;

import hit.dreamer.chatserver.dto.Result;
import hit.dreamer.chatserver.service.RoomService;
import hit.dreamer.chatserver.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RoomController {

	@Resource
	private RoomService roomService;
	@PostMapping("/rooms")
	public Result getRooms(){
		return roomService.getRooms();
	}
	@GetMapping("/room/messages")
	public Result getMessageByRoomId(@RequestParam("roomId")Long roomId){
		return roomService.getMessageByRoomId(roomId);
	}
}
