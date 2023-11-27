package hit.dreamer.chatserver.interceptor;

import hit.dreamer.chatserver.utils.RedisConstants;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;
import java.util.concurrent.TimeUnit;

public class FreshStatusInterceptor implements HandlerInterceptor {

    private StringRedisTemplate stringRedisTemplate;

    public FreshStatusInterceptor(StringRedisTemplate stringRedisTemplate){
        this.stringRedisTemplate = stringRedisTemplate;
    }

    /**
     * 对于所有请求进行拦截，查询用户：
     *  如果查询到用户登录了，则刷新有效期，放行
     *  如果没有登录，放行到后面
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String identifyToken = request.getHeader("authorization");
        Map<Object, Object> UserDTO = stringRedisTemplate.opsForHash().entries(RedisConstants.LOGIN_USER_KEY + identifyToken);
        if (UserDTO.isEmpty()) {
            return true;
        }
        stringRedisTemplate.expire(RedisConstants.LOGIN_USER_KEY + identifyToken, RedisConstants.LOGIN_USER_TTL, TimeUnit.SECONDS);
        return true;
    }
}
