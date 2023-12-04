package hit.dreamer.chatserver.netty.handler;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import hit.dreamer.chatserver.netty.message.ChatMessage;
import hit.dreamer.chatserver.netty.message.SendMessage;
import hit.dreamer.chatserver.utils.ChannelHolder;
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

    public FreshStatusHandler(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame textWebSocketFrame) throws Exception {
        String authorization = (String) ChannelHolder.getChannelAttribute(channelHandlerContext.channel(), "authorization");
        //在redis中刷新有效期
        log.debug("刷新有效期");
        String userId = stringRedisTemplate.opsForHash().get(RedisConstants.LOGIN_USER_KEY + authorization, "id").toString();
        stringRedisTemplate.expire(RedisConstants.LOGIN_USER_KEY + authorization, RedisConstants.LOGIN_USER_TTL, TimeUnit.SECONDS);
        stringRedisTemplate.expire(RedisConstants.LOGIN_USER_KEY + userId, RedisConstants.LOGIN_USER_TTL, TimeUnit.SECONDS);

        ObjectMapper objectMapper = new ObjectMapper();
        SendMessage sendMessage = objectMapper.readValue(textWebSocketFrame.text(), SendMessage.class);
        channelHandlerContext.fireChannelRead(sendMessage);
    }
}
