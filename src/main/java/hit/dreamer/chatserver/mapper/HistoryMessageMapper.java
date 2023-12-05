package hit.dreamer.chatserver.mapper;

import hit.dreamer.chatserver.pojo.HistoryMessage;
import hit.dreamer.chatserver.pojo.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface HistoryMessageMapper {
	/**
	 * 根据发送者查询历史消息
	 * */
	public List<HistoryMessage> getHistoryMessageBySender(Long senderId);
	/**
	 * 插入一条数据
	 * */
	public void insert(HistoryMessage historyMessage);
	/**
	 * 根据群聊删除历史消息
	 * */
	public void deleteByRoomId(Long roomId);
	/**
	 * 根据群聊查询历史消息
	 * */
	public List<HistoryMessage> getHistoryMessageByRoom(Long roomId);
	/**
	 * 根据id查询历史消息
	 * */
	public HistoryMessage getHistoryMessageById(Long id);
}
