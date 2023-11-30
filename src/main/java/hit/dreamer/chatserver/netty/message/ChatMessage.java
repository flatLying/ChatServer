package hit.dreamer.chatserver.netty.message;

import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class ChatMessage {
    private String _id;
    private int indexId;
    private String content;
    private String senderId;
    private String username;
    private String avatar;
    private String date;
    private String timestamp;
    private Boolean system;
    private Boolean saved;
    private Boolean distributed;
    private Boolean seen;
    private Boolean deleted;
    private Boolean failure;
    private Boolean disableActions;
    private Boolean disableReactions;
    private List<Map<String, Object>> files;
    private Map<Object, List<String>> reactions;
    private Map<String, Object> replyMessage;
}
