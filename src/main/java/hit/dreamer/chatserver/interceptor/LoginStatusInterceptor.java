package hit.dreamer.chatserver.interceptor;

import com.fasterxml.jackson.databind.util.BeanUtil;
import hit.dreamer.chatserver.pojo.UserDTO;
import hit.dreamer.chatserver.utils.RedisConstants;
import hit.dreamer.chatserver.utils.UserHolder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

public class LoginStatusInterceptor implements HandlerInterceptor {

    private StringRedisTemplate stringRedisTemplate;

    public LoginStatusInterceptor(StringRedisTemplate stringRedisTemplate){
        this.stringRedisTemplate = stringRedisTemplate;
    }

    /**
     * 取出identifyToken进行用户查询，如果存在，那么放入本地变量，放行
     *  如果不存在，则拒绝访问
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String identifyToken = request.getHeader("authorization");
        Map<Object, Object> userDTOMap = stringRedisTemplate.opsForHash().entries(RedisConstants.LOGIN_USER_KEY + identifyToken);
        if (userDTOMap.isEmpty()) {
            response.setStatus(401);
            return false;
        }
        UserDTO userDTO= new UserDTO();
        BeanUtils.copyProperties(userDTOMap, userDTO);
        UserHolder.saveUserDTO(userDTO);
        return true;
    }

}
