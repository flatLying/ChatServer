package hit.dreamer.chatserver.mapper;

import hit.dreamer.chatserver.entries.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
	/**
	 * 根据手机号查询
	* */
	public User getUserByPhone(String phone);
	/**
	 * 插入一条数据
	 * */
	public void insert(User user);
	/**
	 * 根据手机号和密码查询用户
	 * */
	public User getUserByPhoneAndPassword(String phone,String password);
	
}
