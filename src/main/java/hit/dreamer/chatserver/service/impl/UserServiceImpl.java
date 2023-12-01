package hit.dreamer.chatserver.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.UUID;
import com.apistd.uni.UniException;
import com.apistd.uni.UniResponse;
import com.apistd.uni.sms.UniMessage;
import hit.dreamer.chatserver.dto.LoginFormDTO;
import hit.dreamer.chatserver.dto.Result;
import hit.dreamer.chatserver.dto.UserDTO;
import hit.dreamer.chatserver.pojo.User;
import hit.dreamer.chatserver.mapper.UserMapper;
import hit.dreamer.chatserver.service.UserService;
import hit.dreamer.chatserver.utils.CodeUtils;
import hit.dreamer.chatserver.utils.RedisConstants;
import hit.dreamer.chatserver.utils.RegexUtils;
import io.lettuce.core.pubsub.PubSubOutput;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static io.lettuce.core.pubsub.PubSubOutput.Type.message;


@Slf4j
@Service
public class UserServiceImpl implements UserService {
	
	@Resource
	private UserMapper userMapper;
	@Resource
	private StringRedisTemplate stringRedisTemplate;
	@Override
	public void insert(User user) {
		userMapper.insert(user);
	}
	
	@Override
	public Result sendCode(String phone) {
		//1.校验手机号
		if(RegexUtils.isPhoneInvalid(phone)){
			return Result.fail("手机号格式错误");
		}
		//2.生成验证码
		String code = CodeUtils.generatorCode(phone);
		//3.获取短信验证码平台
		UniMessage message = CodeUtils.codeApi(phone,code);
		// 发送短信
		try {
			UniResponse res = message.send();
			log.debug("验证码发送情况:"+res.toString());
			//3.保存到redis
			stringRedisTemplate.opsForValue().set(RedisConstants.LOGIN_CODE_KEY+phone,code,RedisConstants.LOGIN_CODE_TTL,TimeUnit.MINUTES);
			log.debug("发送验证码为："+code);
		} catch (UniException e) {
			log.debug("Error: " + e);
			log.debug("RequestId: " + e.requestId);
			return Result.fail("验证码发送失败！！！！");
		}
		return Result.ok();
	}
	
	@Override
	public Result loginByPassword(LoginFormDTO loginForm) {
		String phone = loginForm.getPhone();
		String password = loginForm.getPassword();
		//1.校验手机号
		if(RegexUtils.isPhoneInvalid(phone)){
			return Result.fail("手机号格式错误");
		}
		//判断密码是否正确
		User user = userMapper.getUserByPhoneAndPassword(phone, password);
		if (user==null){
			return Result.fail("用户名或密码错误");
		}
		return userToRedis(user);
	}
	
	private Result userToRedis(User user) {
		String userToken = stringRedisTemplate.opsForValue().get(RedisConstants.LOGIN_USER_KEY + user.getId());
		if (userToken == null){
			//5.随机生成token
			String token = UUID.randomUUID().toString(true);
			//6.生成userdto对象
			UserDTO userDTO = new UserDTO(user);
			//7.保存到redis
			String key = RedisConstants.LOGIN_USER_KEY + token;
			Map<String, Object> userMap = BeanUtil.beanToMap(userDTO);
			userMap.forEach((keys, value) -> {
				if (null != value) userMap.put(keys, String.valueOf(value));
			});
			stringRedisTemplate.opsForHash().putAll(key, userMap);
			stringRedisTemplate.opsForValue().set(RedisConstants.LOGIN_USER_KEY + user.getId(), token);
			//设置有效期
			stringRedisTemplate.expire(key,RedisConstants.LOGIN_USER_TTL,TimeUnit.SECONDS);
			stringRedisTemplate.expire(RedisConstants.LOGIN_USER_KEY + user.getId(), RedisConstants.LOGIN_USER_TTL, TimeUnit.SECONDS);
			return Result.ok(token);
		}
		else {
			Result result = new Result();
			result.setData(userToken);
			result.setSuccess(false);
			result.setErrorMsg("用户已经登录");
			return result;
		}
	}
	
	@Override
	public Result loginByCode(LoginFormDTO loginForm) {
		String phone = loginForm.getPhone();
		String code = loginForm.getCode();
		//1.校验手机号
		if(RegexUtils.isPhoneInvalid(phone)){
			return Result.fail("手机号格式错误");
		}
		//2.从redis获取验证码
		String validCode = stringRedisTemplate.opsForValue().get(RedisConstants.LOGIN_CODE_KEY + phone);
		if (validCode==null||!validCode.equals(code)){
			return Result.fail("验证码错误");
		}
		//3.判断用户是否存在
		User user = userMapper.getUserByPhone(phone);
		//4.用户不存在，创建用户
		if (user==null){
			user=createUserByPhone(phone);
		}
		//5.随机生成token
		return userToRedis(user);
	}
	//根据手机号创建新用户
	private User createUserByPhone(String phone){
		User user=new User();
		user.setAvatar("1.jpg");
		user.setLevel((short) 1);
		user.setPassword("123456");
		user.setPhone(phone);
		user.setNickName("彼岸星光");
		user.setCreateTime(LocalDateTime.now());
		user.setUpdateTime(LocalDateTime.now());
		userMapper.insert(user);
		return user;
	}
}
