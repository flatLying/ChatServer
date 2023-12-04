package hit.dreamer.chatserver.netty.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;


public class HandlerInitializer extends ChannelInitializer<Channel> {
    private StringRedisTemplate stringRedisTemplate;
    private RabbitTemplate rabbitTemplate;
    public HandlerInitializer(StringRedisTemplate stringRedisTemplate, RabbitTemplate rabbitTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    protected void initChannel(Channel channel) throws Exception {
        ChannelPipeline pipeline = channel.pipeline();
        pipeline.addLast(new HttpServerCodec());
        pipeline.addLast(new HttpObjectAggregator(65536));
        pipeline.addLast(new LoginStatusHandler(stringRedisTemplate));
        pipeline.addLast(new WebSocketServerProtocolHandler("/chat"));
        pipeline.addLast(new FreshStatusHandler(stringRedisTemplate));
        pipeline.addLast(new MessageTransferHandler(rabbitTemplate));
//        将handler组件添加到pipeline中
    }
}
