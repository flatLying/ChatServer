package hit.dreamer.chatserver.utils;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * 为消息生成全局唯一ID，保证消息的幂等性
 * 保证在线消息和离线消息的id唯一
 * */
@Component
public class RedisIdWorker {
	private final static long BEGIN_TIMPSTAMP=1701388800L;

	private StringRedisTemplate stringRedisTemplate;
	public RedisIdWorker(StringRedisTemplate stringRedisTemplate){
		this.stringRedisTemplate =stringRedisTemplate;
	}
	public Long nextId(){
		//1.生成时间戳
		long timpstamp = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC) - BEGIN_TIMPSTAMP;
		//2.生成序列号
		//每天生成一个key进行自增，原因见redis视频
		String format = LocalDateTime.now().format(DateTimeFormatter.BASIC_ISO_DATE);
		long count = stringRedisTemplate.opsForValue().increment(RedisConstants.MESSAGE_CODE_KEY + format);
		return timpstamp<<32 | count;
	}
	
//	public static void main(String[] args) {
//		LocalDateTime localDateTime = LocalDateTime.of(2023, 12, 1, 0, 0, 0);
//		long epochSecond = localDateTime.toEpochSecond(ZoneOffset.UTC);
//		System.out.println(epochSecond);
//	}
}
