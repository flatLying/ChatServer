package hit.dreamer.chatserver.utils;

import hit.dreamer.chatserver.pojo.UserDTO;

import java.util.Map;

public class UserHolder {
    private static final ThreadLocal<UserDTO> userThreadLocal = new ThreadLocal<>();

    public static void saveUserDTO (UserDTO userDTO){
        userThreadLocal.set(userDTO);
    }

    public static void removeUserDTO(){
        userThreadLocal.remove();
    }

    public static UserDTO getUserDTO(){
        return userThreadLocal.get();
    }
}
