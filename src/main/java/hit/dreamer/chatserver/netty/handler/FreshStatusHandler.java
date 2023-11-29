package hit.dreamer.chatserver.netty.handler;

import hit.dreamer.chatserver.utils.ChannelHolder;
import hit.dreamer.chatserver.utils.RedisConstants;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.Attribute;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.TimeUnit;


@Slf4j
@ChannelHandler.Sharable
@Component
public class FreshStatusHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame textWebSocketFrame) throws Exception {
        log.debug("用户{}刷新成功", ChannelHolder.getChannelAttribute(channelHandlerContext.channel(), "authorization"));
        //在redis中刷新有效期
        channelHandlerContext.writeAndFlush(new TextWebSocketFrame(textWebSocketFrame.text() + "--back message"));
    }
}