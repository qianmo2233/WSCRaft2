package cc.qianmo.wscraft.Player;

import cc.qianmo.wscraft.Utils.DataBase;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerEvent implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (DataBase.getToken(event.getPlayer()) == null) {
            event.getPlayer().sendMessage(ChatColor.RED + "检测到你还没有Token！");
            event.getPlayer().sendMessage(ChatColor.YELLOW + "输入 /wscraft token create 来生成一个Token");
            event.getPlayer().sendMessage( ChatColor.YELLOW + "否则你将无法使用WSCraft的部分功能！");
        }
    }
}
