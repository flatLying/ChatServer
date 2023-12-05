package hit.dreamer.chatserver.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hit.dreamer.chatserver.dto.Result;
import hit.dreamer.chatserver.dto.RoomDTO;
import hit.dreamer.chatserver.dto.SendUserDTO;
import hit.dreamer.chatserver.dto.UserDTO;
import hit.dreamer.chatserver.mapper.RoomMapper;
import hit.dreamer.chatserver.mapper.UserMapper;
import hit.dreamer.chatserver.pojo.Room;
import hit.dreamer.chatserver.pojo.User;
import hit.dreamer.chatserver.service.RoomService;
import hit.dreamer.chatserver.service.UserService;
import hit.dreamer.chatserver.utils.UserHolder;
import jakarta.annotation.Resource;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class RoomServiceImpl implements RoomService {
	@Resource
	private UserService userService;
	@Resource
	private RoomMapper roomMapper;
	@Resource
	private UserMapper userMapper;
	@Override
	public Result getRooms() {
		//获取当前登录用户
		UserDTO userDTO= UserHolder.getUserDTO();
		Long userId=userDTO.getId();
		//查找当前用户所有加入到的群聊
		List<Long> roomIdList = roomMapper.queryRoomIdByUserId(userId);
		List<RoomDTO> roomDTOList = new ArrayList<>();
		roomIdList.forEach(roomId->{
			RoomDTO roomDTO=new RoomDTO();
			//获取当前群聊信息
			Room room = roomMapper.queryRoomInfoById(roomId);
			log.debug("room :"+room);
			roomDTO.setRoomName(room.getName());
			roomDTO.setRoomId(roomId);
			roomDTO.setIndex(RandomUtil.randomInt(0,10));
			roomDTO.setAvatar(room.getAvatar());
			//获取该群聊中所有的用户信息
			List<User> userList = userMapper.getUserByRoomId(roomId);
			List<SendUserDTO> sendUserDTOList=new ArrayList<>();
			userList.forEach(user -> {
				SendUserDTO sendUserDTO=new SendUserDTO();
				sendUserDTO.set_id(user.getId());
				sendUserDTO.setUsername(user.getNickName());
				sendUserDTO.setAvatar(user.getAvatar());
				boolean isLogin=userService.userLogin(user);
				sendUserDTO.setStatus(isLogin,user.getUpdateTime());
				sendUserDTOList.add(sendUserDTO);
			});
			roomDTO.setUsers(sendUserDTOList);
			//获取最新一条消息和未读消息数
//			roomDTO.setLastMessage("""
//				{_id: 'xyz',content: 'Last message received',senderId: '1234',username: 'John Doe',timestamp: '10:20',saved: true,distributed: false,seen: false,new: true}""");
			roomDTO.setUnreadCount(RandomUtil.randomInt(0,4));
			roomDTOList.add(roomDTO);
		});
//		ObjectMapper objectMapper = new ObjectMapper();
//		String roomDTOJson = objectMapper.writeValueAsString(roomDTOList);
		return Result.ok(roomDTOList);
	}
}
