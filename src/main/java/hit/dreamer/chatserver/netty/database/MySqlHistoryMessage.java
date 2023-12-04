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
public class MySqlHistoryMessage {

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "direct.queue2", durable = "true"),
            exchange = @Exchange(name = "HistoryMessage", type = ExchangeTypes.DIRECT),
            key = {"historyMessage"}
    ))
    public void saveHistoryMessage(SendMessage sendMessage){
        log.debug("mysql 历史消息：{}", sendMessage.toString());
    }
}
