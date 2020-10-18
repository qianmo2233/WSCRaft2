package cc.qianmo.wscraft.Events;

import org.java_websocket.WebSocket;

public class MsgEvent extends WSCraftEvent {
    public static String msg;
    public MsgEvent(WebSocket webSocket, String Message) {
        super(webSocket);
        msg = Message;
    }

    public String getMsg() {
        return msg;
    }
}
