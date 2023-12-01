package hit.dreamer.chatserver.netty.handler;

import hit.dreamer.chatserver.utils.ChannelHolder;
import hit.dreamer.chatserver.utils.RedisConstants;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@ChannelHandler.Sharable
@Component
public class LoginStatusHandler extends ChannelInboundHandlerAdapter {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof FullHttpRequest){
            FullHttpRequest request = (FullHttpRequest) msg;
            String authorization = request.headers().get("authorization");
            log.debug("websocket coming~~");
            Map<Object, Object> userDTOMap = stringRedisTemplate.opsForHash().entries(RedisConstants.LOGIN_USER_KEY + authorization);
            if (!userDTOMap.isEmpty()){
                log.debug("验证通过");
                // 在channel的本地保存用户的id信息
                ChannelHolder.setChannelAttribute(ctx.channel(), "authorization", authorization);
                ctx.pipeline().remove(LoginStatusHandler.class);
                ctx.fireChannelRead(msg);
            }
            else {
                log.debug("验证失败");
                ctx.channel().close();
            }
        }
        else {
            log.debug("连接类型不正确");
            ctx.channel().close();
        }
    }
}
