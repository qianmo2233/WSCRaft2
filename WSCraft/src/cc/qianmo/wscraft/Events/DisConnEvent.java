package cc.qianmo.wscraft.Events;

import org.java_websocket.WebSocket;

public class DisConnEvent extends WSCraftEvent {
    public DisConnEvent(WebSocket webSocket) {
        super(webSocket);
    }
}
