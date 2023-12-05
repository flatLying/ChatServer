package hit.dreamer.chatserver.mapper;
import hit.dreamer.chatserver.pojo.Room;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RoomMapper {
	/**
	 * 插入一个新的聊天室
	 * */
	public void insert(Room room);
	/**
	 * 根据userid查询群聊
	 * */
	public List<Long> queryRoomIdByUserId(Long id);
	/**
	 * 根据id查询群聊信息
	 * */
	public Room queryRoomInfoById(Long id);
}
