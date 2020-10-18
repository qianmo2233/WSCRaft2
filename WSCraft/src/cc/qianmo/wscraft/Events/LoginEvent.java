package cc.qianmo.wscraft.Events;

import org.java_websocket.WebSocket;

public class LoginEvent extends WSCraftEvent {
    public LoginEvent(WebSocket webSocket, String Player) {
        super(webSocket);
    }
}
