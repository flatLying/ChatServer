package hit.dreamer.chatserver.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hit.dreamer.chatserver.netty.message.ChatMessage;
import hit.dreamer.chatserver.pojo.HistoryMessage;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import java.time.LocalDateTime;

import java.util.Map;

public class MessageTypeUtils {
    private static ObjectMapper objectMapper = new ObjectMapper();
    public static ChatMessage TextWebSocketFrame2ChatMessage(TextWebSocketFrame textWebSocketFrame) throws JsonProcessingException {
        Map socketFrameMap = objectMapper.readValue(textWebSocketFrame.text(), Map.class);
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setContent((socketFrameMap.get("content") == null) ? "" : (String) socketFrameMap.get("content"));
        chatMessage.setFiles(socketFrameMap.get("files")==null ? "" : socketFrameMap.get("files").toString());
        chatMessage.setReplyMessage(socketFrameMap.get("replyMessage")==null ? "" : socketFrameMap.get("replyMessage").toString());
        chatMessage.setUsersTag(socketFrameMap.get("usersTag")==null? "" : socketFrameMap.get("usersTag").toString());
        chatMessage.setRoomId(Long.valueOf(String.valueOf(socketFrameMap.get("roomId"))));
        LocalDateTime now = LocalDateTime.now();
        chatMessage.setDate(TimeUtils.getDatefromLocalDateTime(now));
        chatMessage.setTimestamp(TimeUtils.getTimestampfromLocalDateTime(now));
        return chatMessage;
    }

}
