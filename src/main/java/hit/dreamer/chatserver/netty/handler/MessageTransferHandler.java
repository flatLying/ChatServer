package hit.dreamer.chatserver.netty.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import hit.dreamer.chatserver.mapper.UserMapper;
import hit.dreamer.chatserver.netty.message.SendMessage;
import hit.dreamer.chatserver.pojo.User;
import hit.dreamer.chatserver.utils.ChannelHolder;
import hit.dreamer.chatserver.utils.RedisConstants;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@ChannelHandler.Sharable
public class MessageTransferHandler extends SimpleChannelInboundHandler<SendMessage> {
    private RabbitTemplate rabbitTemplate;
    private UserMapper userMapper;
    private StringRedisTemplate stringRedisTemplate;
    private ObjectMapper objectMapper = new ObjectMapper();

    public MessageTransferHandler(RabbitTemplate rabbitTemplate, UserMapper userMapper, StringRedisTemplate stringRedisTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        this.userMapper = userMapper;
        this.stringRedisTemplate = stringRedisTemplate;
    }
    @SneakyThrows
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, SendMessage sendMessage){
        Long roomId = sendMessage.getRoomId();

        //先找出发信人的id
        Object authorization = ChannelHolder.getChannelAttribute(channelHandlerContext.channel(), "authorization");
        Object senderId_hat = stringRedisTemplate.opsForHash().get(RedisConstants.LOGIN_USER_KEY + (String) authorization, "id");
        Long senderId = Long.valueOf(String.valueOf(senderId_hat));
        //把房间中的人，排除sender，并确认sender是房间中的人
        List<User> usersByRoomId = userMapper.getUserByRoomId(roomId);
        Boolean isSenderInRoom = false;

        for (User user : usersByRoomId){
            if (user.getId().equals(senderId)){
                isSenderInRoom = true;
                usersByRoomId.remove(user);
                break;
            }
        }
        if (isSenderInRoom == false){
            log.debug("发送者不在房间里，恶意构造数据，断开连接");
            channelHandlerContext.close();
        }

        //放入历史消息队列，给所有在线的人发一份
        sendMessage.setSendTime(LocalDateTime.now().toString());
        sendMessage.setSenderId(senderId);

        rabbitTemplate.convertAndSend("Message", "historyMessage", sendMessage);
        for (User user : usersByRoomId){
            String auth = stringRedisTemplate.opsForValue().get(RedisConstants.LOGIN_USER_KEY + user.getId());
            if (auth == null || ChannelHolder.getChannelByUserAuth(auth) == null){
                //这个用户不在线,则给离线消息发
                Long userId = user.getId();
                sendMessage.setReceiverId(userId);
                rabbitTemplate.convertAndSend("Message", "offlineMessage", sendMessage);
            }else {
                //用户在线，则找到channel发过去
                Channel channelByUserAuth = ChannelHolder.getChannelByUserAuth(auth);
                channelByUserAuth.writeAndFlush(objectMapper.writeValueAsString(sendMessage));
            }
        }
//        log.debug("sender room id:{}", roomId);
//        rabbitTemplate.convertAndSend("OffLineMessage", "offlineMessage", sendMessage);
//        rabbitTemplate.convertAndSend("HistoryMessage", "historyMessage", sendMessage);
    }
}
