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
        String authorization = (String) ChannelHolder.getChannelAttribute(channelHandlerContext.channel(), "authorization");
        //在redis中刷新有效期
        String userId = stringRedisTemplate.opsForHash().get(RedisConstants.LOGIN_USER_KEY + authorization, "id").toString();
        stringRedisTemplate.expire(RedisConstants.LOGIN_USER_KEY + authorization, RedisConstants.LOGIN_USER_TTL, TimeUnit.SECONDS);
        stringRedisTemplate.expire(RedisConstants.LOGIN_USER_KEY + userId, RedisConstants.LOGIN_USER_TTL, TimeUnit.SECONDS);
        channelHandlerContext.writeAndFlush(new TextWebSocketFrame(textWebSocketFrame.text() + "--back message"));
    }
}
