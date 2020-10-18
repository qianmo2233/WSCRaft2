package cc.qianmo.wscraft.WebSocket;

import cc.qianmo.wscraft.Auth;
import cc.qianmo.wscraft.Events.ConnEvent;
import cc.qianmo.wscraft.Events.DisConnEvent;
import cc.qianmo.wscraft.Events.LoginEvent;
import cc.qianmo.wscraft.Events.MsgEvent;
import cc.qianmo.wscraft.Utils.DataBase;
import cc.qianmo.wscraft.Utils.Json;
import cc.qianmo.wscraft.Utils.RandomString;
import cc.qianmo.wscraft.Main;
import org.bukkit.Bukkit;
import org.java_websocket.WebSocket;
import org.java_websocket.drafts.Draft;
import org.java_websocket.exceptions.InvalidDataException;
import org.java_websocket.framing.CloseFrame;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.handshake.ServerHandshakeBuilder;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;

public class Server extends WebSocketServer {

    //绑定端口
    public Server(int port) {
        super(new InetSocketAddress(port));
    }

    /**
     * @param conn WebSocket连接
     * @param handshake 客户端握手
     */
    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        String ID = RandomString.getRandomString(10);
        String Token = handshake.getResourceDescriptor().substring(1);
        ConnPool.add(conn, ID);
        if (Token.equals("public")) {
            Main.getInstance().getLogger().info("ID:" + ID);
            ConnEvent event = new ConnEvent(conn);
            Bukkit.getPluginManager().callEvent(event);
        } else {
            Auth.Login(Token, conn);
            Main.getInstance().getLogger().info("用户：" + Auth.getPlayerName(conn) + "登录成功");
            LoginEvent event = new LoginEvent(conn, Auth.getPlayerName(conn));
            Bukkit.getPluginManager().callEvent(event);
        }
    }


    /**
     * @param conn WebSocket连接
     * @param code 状态码
     * @param reason 断开原因
     * @param b 没用的东西
     */
    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean b) {
        Main.getInstance().getLogger().info("检测到连接断开");
        Main.getInstance().getLogger().info("ID:" + ConnPool.getID(conn));
        ConnPool.remove(conn);
        if (Auth.isLogged(conn)) {
            Auth.Logout(conn);
        }
        DisConnEvent event = new DisConnEvent(conn);
        Bukkit.getPluginManager().callEvent(event);
    }


    /**
     * @param conn WebSocket连接
     * @param message 消息
     */
    @Override
    public void onMessage(WebSocket conn, String message) {
        if(Json.isJson(message)) {
            MsgEvent event = new MsgEvent(conn, message);
            Bukkit.getPluginManager().callEvent(event);
        } else {
            Main.getInstance().getLogger().warning("数据错误,请使用Json字符串格式传输数据");
            Main.getInstance().getLogger().warning("拦截的数据:" + message);
        }
    }

    /**
     * @param conn WebSocket连接
     * @param e 异常
     */
    @Override
    public void onError(WebSocket conn, Exception e) {
        Main.getInstance().getLogger().warning("WebSocket服务出错！");
        Main.getInstance().getLogger().warning("错误类型：" + e);
        Main.getInstance().getLogger().warning("触发ID：" + ConnPool.getID(conn));
    }

    @Override
    public void onStart() {
        Main.getInstance().getLogger().info("正在启动WebSocket服务");
        Main.getInstance().getLogger().info("监听端口:" + getPort());
    }

    @Override
    public ServerHandshakeBuilder onWebsocketHandshakeReceivedAsServer(WebSocket conn, Draft draft, ClientHandshake request) throws InvalidDataException {
        ServerHandshakeBuilder builder = super.onWebsocketHandshakeReceivedAsServer(conn, draft, request);
        String s = request.getResourceDescriptor();
        if (s.equals("/")) {
            Main.getInstance().getLogger().info("拒绝了一个连接");
            throw new InvalidDataException(CloseFrame.POLICY_VALIDATION, "非法连接");
        }
        if (s.equals("/public")) {
            Main.getInstance().getLogger().info("有新公共连接");
            return builder;
        }
        if (!DataBase.isExists(s.substring(1))) {
            throw new InvalidDataException(CloseFrame.POLICY_VALIDATION, "Token验证失败");
        }
        return builder;
    }
}
