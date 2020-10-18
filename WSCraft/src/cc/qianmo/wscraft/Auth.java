package cc.qianmo.wscraft;

import cc.qianmo.wscraft.Events.LoginEvent;
import cc.qianmo.wscraft.Utils.DataBase;
import cc.qianmo.wscraft.Utils.Json;
import cc.qianmo.wscraft.WebSocket.ConnPool;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import org.apache.commons.lang.StringEscapeUtils;
import org.bukkit.Bukkit;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Auth {

    private static final BiMap<WebSocket, String> WsPlayer = HashBiMap.create();
    private static final BiMap<String, String> IDPlayer = HashBiMap.create();

    public static void Login(String Token, WebSocket webSocket) {
        WsPlayer.put(webSocket, DataBase.getPlayer(Token));
        IDPlayer.put(ConnPool.getID(webSocket), DataBase.getPlayer(Token));
        loginSuccess(DataBase.getPlayer(Token));
    }

    private static void loginSuccess(String player) {
        Map map = new HashMap();
        map.put("code", "200");
        map.put("result", "LoginSuccess");
        String msg = Json.toJson(map);
        API.sendToPlayer(player, StringEscapeUtils.unescapeJava(msg));
    }

    public static boolean isLogged(WebSocket webSocket) {
        return WsPlayer.containsKey(webSocket);
    }

    public static boolean isLogged(String player) {
        return WsPlayer.inverse().containsKey(player);
    }

    public static String getPlayerName(WebSocket webSocket) {
        return WsPlayer.get(webSocket);
    }

    public static String getPlayerName(String ID) {
        return IDPlayer.get(ID);
    }

    public static void Logout(WebSocket webSocket) {
        WsPlayer.remove(webSocket);
        IDPlayer.inverse().remove(WsPlayer.get(webSocket));
    }

    public static void Logout(String Player) {
        WsPlayer.inverse().remove(Player);
        IDPlayer.inverse().remove(Player);
    }

    public static boolean Send(String Player, String message) {
        if(IDPlayer.inverse().containsKey(Player)) {
            return ConnPool.sendMessage(IDPlayer.inverse().get(Player), message);
        }
        return false;
    }

    public static boolean Send(String message) {
        Set<WebSocket> keySet = WsPlayer.keySet();
        try {
            synchronized (keySet) {
                for (WebSocket conn : keySet) {
                    String ID = WsPlayer.get(conn);
                    if (conn != null) {
                        conn.send(message);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static Map getMap() {
        return IDPlayer;
    }
}
