package hit.dreamer.chatserver.netty.handler;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import hit.dreamer.chatserver.netty.message.ChatMessage;
import hit.dreamer.chatserver.utils.ChannelHolder;
import hit.dreamer.chatserver.utils.MessageTypeUtils;
import hit.dreamer.chatserver.utils.RedisConstants;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.Attribute;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.TimeUnit;


@Slf4j
@ChannelHandler.Sharable
public class FreshStatusHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    private StringRedisTemplate stringRedisTemplate;
    private ObjectMapper objectMapper = new ObjectMapper();

    public FreshStatusHandler(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame textWebSocketFrame) throws Exception {
        String authorization = (String) ChannelHolder.getChannelAttribute(channelHandlerContext.channel(), "authorization");
        //在redis中刷新有效期
//        log.debug("刷新有效期");
        Map<Object, Object> entries = stringRedisTemplate.opsForHash().entries(RedisConstants.LOGIN_USER_KEY + authorization);
        String userId = stringRedisTemplate.opsForHash().get(RedisConstants.LOGIN_USER_KEY + authorization, "id").toString();
        stringRedisTemplate.expire(RedisConstants.LOGIN_USER_KEY + authorization, RedisConstants.LOGIN_USER_TTL, TimeUnit.SECONDS);
        stringRedisTemplate.expire(RedisConstants.LOGIN_USER_KEY + userId, RedisConstants.LOGIN_USER_TTL, TimeUnit.SECONDS);

        ChatMessage chatMessage = MessageTypeUtils.TextWebSocketFrame2ChatMessage(textWebSocketFrame);
        chatMessage.setSenderId(Long.valueOf(userId));
        chatMessage.setUsername((String) entries.get("nickName"));
        chatMessage.setAvatar((String) entries.get("avatar"));

        channelHandlerContext.fireChannelRead(chatMessage);
    }
}
