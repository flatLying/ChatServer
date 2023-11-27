package hit.dreamer.chatserver.dto;

import hit.dreamer.chatserver.entries.User;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDTO {
	private Long id;
	private String nickName;
	private String avatar;
	private Short level;
	public UserDTO(User user){
		this.id=user.getId();
		this.avatar=user.getAvatar();
		this.nickName=user.getNickName();
		this.level=user.getLevel();
	}
}
