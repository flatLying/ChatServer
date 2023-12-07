package hit.dreamer.chatserver.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hit.dreamer.chatserver.dto.Result;
import hit.dreamer.chatserver.dto.RoomDTO;
import hit.dreamer.chatserver.dto.SendUserDTO;
import hit.dreamer.chatserver.dto.UserDTO;
import hit.dreamer.chatserver.mapper.HistoryMessageMapper;
import hit.dreamer.chatserver.mapper.RoomMapper;
import hit.dreamer.chatserver.mapper.UserMapper;
import hit.dreamer.chatserver.netty.message.ChatMessage;
import hit.dreamer.chatserver.pojo.HistoryMessage;
import hit.dreamer.chatserver.pojo.Room;
import hit.dreamer.chatserver.pojo.User;
import hit.dreamer.chatserver.service.RoomService;
import hit.dreamer.chatserver.service.UserService;
import hit.dreamer.chatserver.utils.RedisConstants;
import hit.dreamer.chatserver.utils.TimeUtils;
import hit.dreamer.chatserver.utils.UserHolder;
import jakarta.annotation.Resource;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class RoomServiceImpl implements RoomService {
	private static final long MAX_READ_COUNT=20;
	@Resource
	private UserService userService;
	@Resource
	private RoomMapper roomMapper;
	@Resource
	private UserMapper userMapper;
	@Resource
	private HistoryMessageMapper historyMessageMapper;
	@Resource
	private StringRedisTemplate stringRedisTemplate;
	private ObjectMapper objectMapper = new ObjectMapper();
	@SneakyThrows
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
			Long size = stringRedisTemplate.opsForList().size(RedisConstants.OFFLINE_USER_MESSAGE + userId);
			if(size!=null&&size!=0L){
				roomDTO.setUnreadCount(size);
				String lastMessage = stringRedisTemplate.opsForList().rightPop(RedisConstants.OFFLINE_USER_MESSAGE + userId);
				if (lastMessage != null) {
					stringRedisTemplate.opsForList().rightPush(RedisConstants.OFFLINE_USER_MESSAGE + userId,lastMessage);
				}
				try {
					ChatMessage chatMessage = objectMapper.readValue(lastMessage, ChatMessage.class);
					roomDTO.setLastMessage(chatMessage);
				} catch (JsonProcessingException e) {
					throw new RuntimeException(e);
				}
			}else{
				roomDTO.setUnreadCount(0L);
				roomDTO.setLastMessage(new ChatMessage());
			}
			roomDTOList.add(roomDTO);
		});
		return Result.ok(roomDTOList);
	}
	
	@Override
	public Result getMessageByRoomId(Long roomId) {
		//获取当前用户
		Long userId = UserHolder.getUserDTO().getId();
		//查询当前用户离线消息的数量
		Long size = stringRedisTemplate.opsForList().size(RedisConstants.OFFLINE_USER_MESSAGE + userId);
		List<ChatMessage> messageList =new ArrayList<>();
		//如果当前用户存在离线消息
		if(size!=null&&size!=0L){
			//取出所有的离线消息
			List<String> stringList = stringRedisTemplate.opsForList().range(RedisConstants.OFFLINE_USER_MESSAGE + userId, 0, -1);
//			stringRedisTemplate.delete(RedisConstants.OFFLINE_USER_MESSAGE + userId);
			if (stringList != null) {
				stringList.forEach(s -> {
					try {
						ChatMessage chatMessage = objectMapper.readValue(s, ChatMessage.class);
						messageList.add(chatMessage);
					} catch (JsonProcessingException e) {
						throw new RuntimeException(e);
					}
				});
				log.debug("离线消息数量："+size);
				size=MAX_READ_COUNT-size;
				log.debug("历史消息数量："+size);
				//如果离线消息数量不够MAX_READ_COUNT条，从数据库中补足剩余条数
				if (size>0){
					List<HistoryMessage> historyMessageList = historyMessageMapper.getHistoryMessageByRoom(roomId, size);
					if (!historyMessageList.isEmpty()){
						historyMessageList.forEach(historyMessage -> {
							ChatMessage chatMessage = getChatMessage(historyMessage);
							System.out.println(historyMessage);
							messageList.add(chatMessage);
						});
					}
				}
				//此时离线消息已读，将所有的离线消息转换为历史消息
				stringList.forEach(s -> {
					try {
						ChatMessage chatMessage = objectMapper.readValue(s, ChatMessage.class);
						HistoryMessage historyMessage = new HistoryMessage(chatMessage);
						historyMessageMapper.insert(historyMessage);
					} catch (JsonProcessingException e) {
						throw new RuntimeException(e);
					}
				});
			}
		}else{
			//如果没有离线消息，直接从数据库中读取历史消息
			List<HistoryMessage> historyMessageList = historyMessageMapper.getHistoryMessageByRoom(roomId, MAX_READ_COUNT);
			if (!historyMessageList.isEmpty()){
				historyMessageList.forEach(historyMessage -> {
					System.out.println(historyMessage);
					ChatMessage chatMessage = getChatMessage(historyMessage);
					messageList.add(chatMessage);
				});
			}
		}
		log.debug("messagelist.size:"+messageList);
		return Result.ok(messageList);
	}
	
	private static ChatMessage getChatMessage(HistoryMessage historyMessage) {
		ChatMessage chatMessage = new ChatMessage();
		chatMessage.setRoomId(historyMessage.getRoomId());
		chatMessage.setReplyMessage(historyMessage.getReplyMessage());
		chatMessage.setAvatar(historyMessage.getAvatar());
		chatMessage.setContent(historyMessage.getContent());
		chatMessage.set_id(historyMessage.getId());
		chatMessage.setUsername(historyMessage.getUsername());
		chatMessage.setFiles(historyMessage.getFiles());
		chatMessage.setDate(TimeUtils.getDatefromLocalDateTime(historyMessage.getSendTime()));
		chatMessage.setTimestamp(TimeUtils.getTimestampfromLocalDateTime(historyMessage.getSendTime()));
		chatMessage.setUsersTag(historyMessage.getUsersTag());
		chatMessage.setSenderId(historyMessage.getSenderId());
		chatMessage.setIndexId(RandomUtil.randomLong(0,10000L));
		chatMessage.setReactions(historyMessage.getReactions());
		chatMessage.setSystem(false);
		chatMessage.setSaved(true);
		chatMessage.setSeen(true);
		chatMessage.setDisableActions(false);
		chatMessage.setDisableReactions(false);
		chatMessage.setDeleted(false);
		chatMessage.setFailure(true);
		chatMessage.setDistributed(true);
		return chatMessage;
	}
}
