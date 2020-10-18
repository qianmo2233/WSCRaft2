package cc.qianmo.wscraft.WebSocket;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.sun.istack.internal.NotNull;
import org.java_websocket.WebSocket;

import java.util.Map;
import java.util.Set;

public class ConnPool {

    //创建连接池
    private static final BiMap<WebSocket, String> connPool = HashBiMap.create();

    public static void add(WebSocket conn, String ID) {
        connPool.put(conn, ID);
    }

    public static void remove(WebSocket conn) {
        connPool.remove(conn);
    }

    public static void remove(String ID) {
        connPool.inverse().remove(ID);
    }

    public static WebSocket getConn(String ID) {
        return connPool.inverse().get(ID);
    }

    public static String getID(WebSocket conn) {
        return connPool.get(conn);
    }

    public static boolean sendMessage(@NotNull String ID, @NotNull  String message) {
        WebSocket conn = getConn(ID);
        try {
            conn.send(message);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean sendMessage(@NotNull WebSocket conn, String message) {
        try {
            conn.send(message);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean sendMessage(@NotNull String message) {
        Set<WebSocket> keySet = connPool.keySet();
        synchronized (keySet) {
            for (WebSocket conn : keySet) {
                String ID = connPool.get(conn);
                if (conn != null) {
                    conn.send(message);
                }
            }
            return true;
        }
    }

    public static Map getMap () {
        return connPool;
    }
}
