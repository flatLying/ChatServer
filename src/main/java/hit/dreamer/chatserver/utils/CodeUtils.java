package hit.dreamer.chatserver.utils;
import cn.hutool.core.util.RandomUtil;
import com.apistd.uni.Uni;
import com.apistd.uni.UniException;
import com.apistd.uni.UniResponse;
import com.apistd.uni.sms.UniSMS;
import com.apistd.uni.sms.UniMessage;
import java.util.HashMap;
import java.util.Map;
public class CodeUtils {
	public static String ACCESS_KEY_ID = "PzoEem5YCQM8R5HfMWg58tHKnpLHPvrDFy9ySzJmbzUaF8pqH";
	public static String generatorCode(String phone){
		
		return RandomUtil.randomNumbers(6);
	}
	public static UniMessage codeApi(String phone,String code){
		// 初始化
		Uni.init(ACCESS_KEY_ID); // 若使用简易验签模式仅传入第一个参数即可
		// 设置自定义参数 (变量短信)
		Map<String, String> templateData = new HashMap<String, String>();
		templateData.put("code", code);
		templateData.put("ttl", String.valueOf(RedisConstants.LOGIN_CODE_TTL));
		// 构建信息
		UniMessage message = UniSMS.buildMessage()
				.setTo(phone)
				.setSignature("田雪洋的聊天机器人")
				.setTemplateId("pub_verif_identity_ttl")
				.setTemplateData(templateData);
		return message;
	}
}
