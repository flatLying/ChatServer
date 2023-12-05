package hit.dreamer.chatserver.utils;

public class RedisConstants {
    public static final String LOGIN_CODE_KEY = "chatserver:login:code:";
    public static final Long LOGIN_CODE_TTL = 5L;
    public static final String LOGIN_USER_KEY = "chatserver:login:token:";
    public static final Long LOGIN_USER_TTL = 1800L;

    public static final String OFFLINE_USER_MESSAGE = "chatserver:chat:offlinemessage:";
    
}


