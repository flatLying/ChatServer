package hit.dreamer.chatserver.netty.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SendMessage {
    private String roomId;
    private String content;
    private Object files;
    private Object usersTag;
    private Object replyMessage;
}
