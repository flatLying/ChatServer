package hit.dreamer.chatserver.netty.database;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hit.dreamer.chatserver.mapper.HistoryMessageMapper;
import hit.dreamer.chatserver.netty.message.SendMessage;
import hit.dreamer.chatserver.pojo.HistoryMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class MySqlHistoryMessage {

    @Autowired
    private HistoryMessageMapper historyMessageMapper;

    private ObjectMapper objectMapper = new ObjectMapper();

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "direct.queue2", durable = "true"),
            exchange = @Exchange(name = "HistoryMessage", type = ExchangeTypes.DIRECT),
            key = {"historyMessage"}
    ))
    public void saveHistoryMessage(Map<String, Object> message) throws JsonProcessingException {
        Map<String, Object> headers = (Map<String, Object>) message.get("headers");
        String senderId = (String) headers.get("senderId");
        SendMessage sendMessage = (SendMessage) message.get("content");
        String roomId = sendMessage.getRoomId();

        HistoryMessage historyMessage = new HistoryMessage();
        historyMessage.setRoomId(Long.valueOf(roomId));
        historyMessage.setSenderId(Long.valueOf(senderId));
        historyMessage.setMessage(objectMapper.writeValueAsString(sendMessage));

        historyMessageMapper.insert(historyMessage);
        log.debug("mysql 历史消息：{}", sendMessage);
    }
}
