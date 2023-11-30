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
            Map<Object, Object> userDTOMap = stringRedisTemplate.opsForHash().entries(RedisConstants.LOGIN_USER_KEY + authorization);
            if (userDTOMap.isEmpty()){
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


//    @Override
//    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object msg) throws Exception {
//        if (msg instanceof FullHttpRequest) {
//            String authorization = ((FullHttpRequest) msg).headers().get("authorization");
//            log.debug("用户头{}",authorization);
//            if ("123".equals(authorization)){
//                // 如果校验成功，则不关闭channel，其他什么都不做
//                log.debug("校验成功");
//                channelHandlerContext.channel().attr(UserToken).set(authorization);
//                channelHandlerContext.pipeline().remove(LoginStatusHandler.class);
//            }
//            else {
//                // 校验失败，关闭channel
//                log.debug("校验失败");
//                channelHandlerContext.writeAndFlush(new TextWebSocketFrame("校验失败"));
//                channelHandlerContext.close();
//            }
//        }
//        else {
//            // 校验失败，关闭channel
//            log.debug("参数类型不对，关闭连接");
//            channelHandlerContext.close();
//        }
//    }
}
