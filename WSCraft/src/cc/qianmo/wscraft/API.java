package cc.qianmo.wscraft;

import cc.qianmo.wscraft.Utils.Json;
import cc.qianmo.wscraft.WebSocket.ConnPool;

import java.util.Map;

public class API {

    @Deprecated
    public static boolean Send(String ID, String message) {
        if (Json.isJson(message)) {
            return ConnPool.sendMessage(ID, message);
        }
        return false;
    }

    @Deprecated
    public static boolean Send(String message) {
        if (Json.isJson(message)) {
            return ConnPool.sendMessage(message);
        }
        return false;
    }

    public static boolean sendToPlayer(String Player, String message) {
        return Auth.Send(Player, message);
    }

    public static boolean sendToPlayer(String message) {
        return Auth.Send(message);
    }

    public static Map getAllPlayer() {
        return Auth.getMap();
    }

    public static Map getAll() {
        return ConnPool.getMap();
    }

}
