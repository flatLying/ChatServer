package hit.dreamer.chatserver.netty.database;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hit.dreamer.chatserver.netty.message.ChatMessage;
import hit.dreamer.chatserver.netty.message.MessageWithSenderReceiver;
import hit.dreamer.chatserver.pojo.HistoryMessage;
import hit.dreamer.chatserver.utils.RedisConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class RedisOffLineMessage {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    private ObjectMapper objectMapper = new ObjectMapper();

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "directmessage.queue1", durable = "true"),
            exchange = @Exchange(name = "Message", type = ExchangeTypes.DIRECT),
            key = {"offlineMessage"}
    ))
    public void saveOffLineMessage(MessageWithSenderReceiver message) throws JsonProcessingException {
        Long receiverId = message.getReceiverId();
        ChatMessage chatMessage = message.getChatMessage();
//        HistoryMessage historyMessage = new HistoryMessage(chatMessage);
        stringRedisTemplate.opsForList().rightPush(RedisConstants.OFFLINE_USER_MESSAGE + receiverId, objectMapper.writeValueAsString(chatMessage));
        log.debug("离线消息:{}", chatMessage.toString());
    }
}
