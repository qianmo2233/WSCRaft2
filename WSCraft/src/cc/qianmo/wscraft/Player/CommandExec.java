package cc.qianmo.wscraft.Player;

import cc.qianmo.wscraft.Utils.DataBase;
import cc.qianmo.wscraft.Utils.RandomString;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandExec implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("wscraft")) {
            if (args.length == 1 && args[0].equalsIgnoreCase("help")) {
                commandSender.sendMessage(ChatColor.YELLOW + "======== WSCraft 2.1.1 帮助 ========");
                commandSender.sendMessage(ChatColor.YELLOW + "获取Token     /wscraft token create");
                commandSender.sendMessage(ChatColor.YELLOW + "查看Token     /wscraft token view");
                commandSender.sendMessage(ChatColor.YELLOW + "WSCraft帮助     /wscraft help");
                return true;
            }
            if(args.length == 2 && args[0].equalsIgnoreCase("token")) {
                if (commandSender instanceof Player) {
                    Player player = (Player) commandSender;
                    if (args[1].equalsIgnoreCase("create")) {
                        if (DataBase.getToken(player) == null) {
                            String Token = RandomString.getRandomString(9);
                            DataBase.insert(player.getName(), Token);
                            player.sendMessage(ChatColor.GREEN + "Token生成成功：" + ChatColor.WHITE + Token);
                            return true;
                        }
                        player.sendMessage(ChatColor.RED + "你已经拥有一个Token了");
                        player.sendMessage(ChatColor.YELLOW + "输入 /wscraft token view 来查看你的Token");
                        return true;
                    }
                    if (args[1].equalsIgnoreCase("view")) {
                        String Token = DataBase.getToken(player);
                        if (Token == null) {
                            player.sendMessage(ChatColor.RED + "你还没有生成Token");
                            player.sendMessage(ChatColor.YELLOW + "输入 /wscraft token create 来生成一个Token");
                            return true;
                        }
                        player.sendMessage(ChatColor.YELLOW + "你的Token为：" + Token);
                        return true;
                    }
                } else {
                    commandSender.sendMessage("仅玩家才可执行此命令");
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
        return false;
    }
}
