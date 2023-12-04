package hit.dreamer.chatserver.netty.database;

import hit.dreamer.chatserver.netty.message.SendMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RedisOffLineMessage {

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "direct.queue1", durable = "true"),
            exchange = @Exchange(name = "OffLineMessage", type = ExchangeTypes.DIRECT),
            key = {"offlineMessage"}
    ))
    public void saveOffLineMessage(SendMessage sendMessage){
        log.debug("redis 缓存offline:{}", sendMessage.toString());
        return;
    }
}
