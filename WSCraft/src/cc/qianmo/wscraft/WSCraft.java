package cc.qianmo.wscraft;

import cc.qianmo.wscraft.Events.ConnEvent;
import cc.qianmo.wscraft.Events.DisConnEvent;
import cc.qianmo.wscraft.Events.LoginEvent;
import cc.qianmo.wscraft.Events.MsgEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.java_websocket.WebSocket;

public abstract class WSCraft implements Listener {
    public static String ID;
    public static String Name;
    public static String Msg;
    public static WebSocket conn;

    public abstract void onMsg(String ID, String Name, WebSocket conn, String Msg);
    public abstract void onConn(String ID, String Name, WebSocket conn);
    public abstract void onLogin(String ID, String Name, WebSocket conn);
    public abstract void onDisConn(String ID, String Name);

    @EventHandler
    public final void onMsgEvent(MsgEvent event) {
        ID = event.getID();
        Name = event.getPlayerName();
        Msg = event.getMsg();
        conn = event.getConn();
        onMsg(ID, Name, conn, Msg);
    }

    @EventHandler
    public final void onConnEvent(ConnEvent event) {
        ID = event.getID();
        Name = event.getPlayerName();
        conn = event.getConn();
        onConn(ID, Name, conn);
    }

    @EventHandler
    public final void onDisConnEvent(DisConnEvent event) {
        ID = event.getID();
        Name = event.getPlayerName();
        conn = event.getConn();
        onDisConn(ID, Name);
    }

    @EventHandler
    public final void onLoginEvent(LoginEvent event) {
        ID = event.getID();
        Name = event.getPlayerName();
        conn = event.getConn();
        onLogin(ID, Name, conn);
    }
}
