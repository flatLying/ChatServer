package hit.dreamer.chatserver.mapper;
import hit.dreamer.chatserver.pojo.Room;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RoomMapper {
	/**
	 * 插入一个新的聊天室
	 * */
	public void insert(Room room);
}
