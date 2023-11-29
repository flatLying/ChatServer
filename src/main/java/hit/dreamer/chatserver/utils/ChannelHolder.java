package hit.dreamer.chatserver.utils;

import io.netty.channel.Channel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ChannelHolder {
    private static final ConcurrentHashMap<String, Channel> UserAuth2Channel = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<Channel, String> Channel2UserAuth = new ConcurrentHashMap<>();

    //这个表示每一个channel中的变量map
    private static final Map<Channel, Map<String, Object>> ChannelAttribute = new ConcurrentHashMap<>();


    public static Channel getChannelByUserAuth(String Authorization){
        return UserAuth2Channel.get(Authorization);
    }

    public static String getUserAuthByChannel(Channel channel){
        return Channel2UserAuth.get(channel);
    }

    public static void bindUserAuthChannel(String Authorizatoin, Channel channel){
        UserAuth2Channel.put(Authorizatoin, channel);
        Channel2UserAuth.put(channel, Authorizatoin);
        ChannelAttribute.put(channel, new ConcurrentHashMap<>());
    }

    public static void unbindUserAuthChannel(Channel channel){
        String Authorization = Channel2UserAuth.remove(channel);
        UserAuth2Channel.remove(Authorization);
        ChannelAttribute.remove(channel);
    }

    public static void setChannelAttribute(Channel channel, String key, Object value){
        ChannelAttribute.get(channel).put(key, value);
    }

    public static Object getChannelAttribute(Channel channel, String key){
        return ChannelAttribute.get(channel).get(key);
    }
}
