package hit.dreamer.chatserver.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.security.Principal;
import java.time.LocalDateTime;


@Data
public class User {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * 主键
	 */
	private Long id;
	
	/**
	 * 手机号码
	 */
	private String phone;
	
	/**
	 * 密码，加密存储
	 */
	private String password;
	
	/**
	 * 昵称，默认是随机字符
	 */
	private String nickName;
	
	/**
	 * 用户头像
	 */
	private String avatar = "";
	
	/**
	 * 创建时间
	 */
	private LocalDateTime createTime;
	
	/**
	 * 更新时间
	 */
	private LocalDateTime updateTime;
	/**
	 * 用户等级
	 */
	private Short level;
}
