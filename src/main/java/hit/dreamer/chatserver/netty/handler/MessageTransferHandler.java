package hit.dreamer.chatserver.netty.handler;

import hit.dreamer.chatserver.netty.message.ChatMessage;
import hit.dreamer.chatserver.netty.message.SendMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

@Slf4j
public class MessageTransferHandler extends SimpleChannelInboundHandler<SendMessage> {
    private RabbitTemplate rabbitTemplate;
    public MessageTransferHandler(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, SendMessage sendMessage) throws Exception {
        String roomId = sendMessage.getRoomId();
        log.debug("sender room id:{}", roomId);
        rabbitTemplate.convertAndSend("OffLineMessage", "offlineMessage", sendMessage);
        rabbitTemplate.convertAndSend("HistoryMessage", "historyMessage", sendMessage);
    }
}
