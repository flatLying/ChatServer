package hit.dreamer.chatserver;

import hit.dreamer.chatserver.mapper.HistoryMessageMapper;
import hit.dreamer.chatserver.mapper.RoomMapper;
import hit.dreamer.chatserver.pojo.HistoryMessage;
import hit.dreamer.chatserver.pojo.Room;
import hit.dreamer.chatserver.pojo.User;
import hit.dreamer.chatserver.mapper.UserMapper;
import hit.dreamer.chatserver.service.UserService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest
class ChatServerApplicationTests {
    
    @Resource
    private UserService userService;
    @Resource
    private UserMapper userMapper;
    @Resource
    private RoomMapper roomMapper;
    @Resource
    private HistoryMessageMapper historyMessageMapper;
    @Test
    void testInsertUser() {
        for (int i = 0; i < 10; i++) {
            User user=new User();
            user.setAvatar(i+".jpg");
            user.setLevel((short) 1);
            user.setPassword("123456");
            user.setPhone("1393838383"+i);
            user.setNickName("测试机器人"+i);
            user.setCreateTime(LocalDateTime.now());
            user.setUpdateTime(LocalDateTime.now());
            userMapper.insert(user);
        }
    }
    @Test
    void testGetByPhone(){
        String phone="18804618032";
        User user = userMapper.getUserByPhone(phone);
        System.out.println(user);
    }
    @Test
    void testInsertRoom(){
        for (int i=0;i<=5;++i){
            String name="TestRoom_"+i;
            String avatar=i+".jpg";
            Room room=new Room();
            room.setRoomName(name);
            room.setAvatar(avatar);
            room.setCreateTime(LocalDateTime.now());
            room.setUpdateTime(LocalDateTime.now());
            roomMapper.insert(room);
        }
    }
    @Test
    void testHistoryMessageInsert(){
        HistoryMessage historyMessage1=new HistoryMessage();
        historyMessage1.setRoomId(1L);
        historyMessage1.setSenderId(1L);
    }
}
