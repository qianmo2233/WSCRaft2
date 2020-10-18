package cc.qianmo.wscraft.Events;

import cc.qianmo.wscraft.Auth;
import cc.qianmo.wscraft.WebSocket.ConnPool;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.java_websocket.WebSocket;

public abstract class WSCraftEvent extends Event {

    public static WebSocket conn;

    public WSCraftEvent (WebSocket webSocket) {
        conn = webSocket;
    }
    private static final HandlerList handlers = new HandlerList();
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
    public static HandlerList getHandlerList() {
        return handlers;
    }

    public String getID() {
        return ConnPool.getID(conn);
    }

    public String getPlayerName() {
        if(Auth.isLogged(conn)) {
            return Auth.getPlayerName(ConnPool.getID(conn));
        }
        return null;
    }

    public WebSocket getConn() {
        return conn;
    }
}
