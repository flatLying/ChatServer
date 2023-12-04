package hit.dreamer.chatserver.netty.handler;

import hit.dreamer.chatserver.utils.ChannelHolder;
import hit.dreamer.chatserver.utils.RedisConstants;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

@Slf4j
@ChannelHandler.Sharable
public class LoginStatusHandler extends ChannelInboundHandlerAdapter {
    private StringRedisTemplate stringRedisTemplate;

    public LoginStatusHandler(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof FullHttpRequest){
            FullHttpRequest request = (FullHttpRequest) msg;
            String uri = request.uri();
            // 解析 URI 以获取 token
            String authorization = extractTokenFromUri(uri);
            log.debug("websocket coming~~：{}", authorization);
            Map<Object, Object> userDTOMap = stringRedisTemplate.opsForHash().entries(RedisConstants.LOGIN_USER_KEY + authorization);
            if (!userDTOMap.isEmpty()){
                ChannelHolder.bindUserAuthChannel(authorization, ctx.channel());
                String modifiedUri = removeTokenFromUri(uri);
                ((HttpRequest) msg).setUri(modifiedUri);
                log.debug("验证通过,设置新的uri:{}", modifiedUri);
                // 在channel的本地保存用户的id信息
                ChannelHolder.setChannelAttribute(ctx.channel(), "authorization", authorization);
                ctx.pipeline().remove(LoginStatusHandler.class);

                ctx.fireChannelRead(msg);
            }
            else {
                log.debug("验证失败");
                send(ctx,"登录失败！",HttpResponseStatus.UNAUTHORIZED);
//                ctx.channel().close();
            }
        }
        else {
            log.debug("连接类型不正确");
            ctx.channel().close();
        }
    }

    //发送http请求，https://juejin.cn/post/7143608389023563806
    private void send(ChannelHandlerContext ctx, String context,
                      HttpResponseStatus status) {
        FullHttpResponse response = new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1,status,
                Unpooled.copiedBuffer(context, CharsetUtil.UTF_8)
        );
        response.headers().set(HttpHeaderNames.CONTENT_TYPE,"text/plain;charset=UTF-8");
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    private String extractTokenFromUri(String uri) {
        // 解析 URI 以提取 token
        // 例如: ws://localhost:8080/chat?token=xxx
        // 实际的解析逻辑依赖于 URI 的格式
        return uri.substring(uri.indexOf("token=") + 6);
    }

    private String removeTokenFromUri(String uri) throws URISyntaxException {
        URI oldUri = new URI(uri);
        QueryStringDecoder decoder = new QueryStringDecoder(uri);
        QueryStringEncoder encoder = new QueryStringEncoder(oldUri.getPath());

        decoder.parameters().forEach((key, value) -> {
            if (!"token".equals(key)) {
                value.forEach(v -> encoder.addParam(key, v));
            }
        });
        return encoder.toString();
    }

}
