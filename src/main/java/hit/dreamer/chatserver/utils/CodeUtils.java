package hit.dreamer.chatserver.utils;


import cn.hutool.core.util.RandomUtil;

public class CodeUtils {
	public static String generatorCode(String phone){
		return RandomUtil.randomNumbers(6);
	}
}
