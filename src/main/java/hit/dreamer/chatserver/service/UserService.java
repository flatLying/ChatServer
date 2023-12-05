package hit.dreamer.chatserver.service;

import hit.dreamer.chatserver.dto.LoginFormDTO;
import hit.dreamer.chatserver.dto.Result;
import hit.dreamer.chatserver.pojo.User;

public interface UserService {
	/**
	 * 插入一个新用户
	 * */
	public void insert(User user);
	
	Result sendCode(String phone);
	
	Result loginByPassword(LoginFormDTO loginForm);
	
	Result loginByCode(LoginFormDTO loginForm);
	
	public boolean userLogin(User user);
}
