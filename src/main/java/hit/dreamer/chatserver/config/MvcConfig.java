package hit.dreamer.chatserver.config;

import hit.dreamer.chatserver.interceptor.FreshStatusInterceptor;
import hit.dreamer.chatserver.interceptor.LoginStatusInterceptor;
import jakarta.annotation.Resources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new FreshStatusInterceptor(stringRedisTemplate))
                .addPathPatterns("/**");
        registry.addInterceptor(new LoginStatusInterceptor(stringRedisTemplate))
                .excludePathPatterns("/user/code","/user/login","/user/login2");
    }
}
