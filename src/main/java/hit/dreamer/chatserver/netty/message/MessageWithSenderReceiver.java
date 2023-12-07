package hit.dreamer.chatserver.netty.message;

import hit.dreamer.chatserver.pojo.HistoryMessage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageWithSenderReceiver implements Serializable {

    public MessageWithSenderReceiver(ChatMessage chatMessage){
        this.chatMessage = chatMessage;
    }
    private Long receiverId;
    private ChatMessage chatMessage;
}
