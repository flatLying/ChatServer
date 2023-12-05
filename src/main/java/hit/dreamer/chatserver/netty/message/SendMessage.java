package hit.dreamer.chatserver.netty.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SendMessage {
    private Long roomId;
    private String content;
    private Object files;
    private Object usersTag;
    private Object replyMessage;

    // 下面三个属性是为了发送到数据库设置的
    private String sendTime;
    private Long senderId;
    private Long receiverId;
}
