package hit.dreamer.chatserver.controller;

import hit.dreamer.chatserver.dto.LoginFormDTO;
import hit.dreamer.chatserver.dto.Result;
import hit.dreamer.chatserver.pojo.User;
import hit.dreamer.chatserver.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
	@Autowired
	private UserService userService;
	/**
	 * 插入新的用户
	 * */
	@PostMapping
	public Result insert(User user){
		userService.insert(user);
		return Result.ok();
	}
	/**
	 * 发送手机验证码
	 */
	@PostMapping("/code")
	public Result sendCode(@RequestParam("phone") String phone, HttpSession session) {
		// TODO 发送短信验证码并保存验证码
		
		return userService.sendCode(phone);
	}
	
	/**
	 * 登录功能
	 * @param loginForm 登录参数，包含手机号、验证码；或者手机号、密码
	 */
	@PostMapping("/login")
	public Result loginByCode(@RequestBody LoginFormDTO loginForm, HttpSession session){
		// TODO 实现登录功能
		return userService.loginByCode(loginForm);
	}
	/**
	 * 登录功能
	 * @param loginForm 登录参数，包含手机号、验证码；或者手机号、密码
	 */
	@PostMapping("/login2")
	public Result loginByPassWord(@RequestBody LoginFormDTO loginForm, HttpSession session){
		// TODO 实现登录功能
		return userService.loginByPassword(loginForm);
	}
}
