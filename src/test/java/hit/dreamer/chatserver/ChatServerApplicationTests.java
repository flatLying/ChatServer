package hit.dreamer.chatserver;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
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

    @Test
    void testRabbitMQConnection(){
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("124.71.32.241");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("adminhit");
        connectionFactory.setPassword("Hit_dreamer123_rabbitmq_complex_upup");
        try {
            Connection connection = connectionFactory.newConnection();
            System.out.println("Successfully connected to RabbitMQ server.");
            connection.close();
        } catch (Exception e) {
            System.err.println("Connection failed: " + e.getMessage());
        }
    }


}
