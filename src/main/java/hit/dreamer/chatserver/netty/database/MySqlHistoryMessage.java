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
            value = @Queue(name = "directmessage.queue2", durable = "true"),
            exchange = @Exchange(name = "Message", type = ExchangeTypes.DIRECT),
            key = {"historyMessage"}
    ))
    public void saveHistoryMessage(SendMessage message) throws JsonProcessingException {
        log.debug("历史消息收到啦。。。。。。");
        Long senderId = message.getSenderId();
        Long roomId = message.getRoomId();
        HistoryMessage historyMessage = new HistoryMessage();
        historyMessage.setRoomId(roomId);
        historyMessage.setSenderId(senderId);
        historyMessage.setMessage(objectMapper.writeValueAsString(message));

        historyMessageMapper.insert(historyMessage);
        log.debug("mysql 历史消息：{}", historyMessage.toString());
    }
}
