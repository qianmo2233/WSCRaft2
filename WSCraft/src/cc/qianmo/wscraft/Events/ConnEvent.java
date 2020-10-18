package cc.qianmo.wscraft.Events;

import org.java_websocket.WebSocket;

public class ConnEvent extends WSCraftEvent {
    public ConnEvent(WebSocket webSocket) {
        super(webSocket);
    }
}
