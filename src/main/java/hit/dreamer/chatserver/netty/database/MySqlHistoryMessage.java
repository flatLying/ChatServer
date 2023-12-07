package hit.dreamer.chatserver.netty.database;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hit.dreamer.chatserver.mapper.HistoryMessageMapper;
import hit.dreamer.chatserver.netty.message.ChatMessage;
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
    public void saveHistoryMessage(ChatMessage chatMessage) throws JsonProcessingException {
        HistoryMessage historyMessage = new HistoryMessage(chatMessage);
        historyMessageMapper.insert(historyMessage);
        log.debug("历史消息:{}", historyMessage.toString());
    }
}
