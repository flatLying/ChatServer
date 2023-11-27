package hit.dreamer.chatserver;

import hit.dreamer.chatserver.entries.User;
import hit.dreamer.chatserver.mapper.UserMapper;
import hit.dreamer.chatserver.service.UserService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.index.PathBasedRedisIndexDefinition;

import java.time.LocalDateTime;

@SpringBootTest
class ChatServerApplicationTests {
    
    @Resource
    private UserService userService;
    @Resource
    private UserMapper userMapper;
    @Test
    void testInsert() {
        User user=new User();
        user.setAvatar("1.jpg");
        user.setLevel((short) 1);
        user.setPassword("123456");
        user.setPhone("18804618031");
        user.setNickName("彼岸星光");
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        userService.insert(user);
    }
    @Test
    void testGetByPhone(){
        String phone="18804618032";
        User user = userMapper.getUserByPhone(phone);
        System.out.println(user);
    }

}
