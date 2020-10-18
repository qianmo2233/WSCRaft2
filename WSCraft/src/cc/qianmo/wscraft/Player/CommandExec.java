package cc.qianmo.wscraft.Player;

import cc.qianmo.wscraft.Utils.DataBase;
import cc.qianmo.wscraft.Utils.RandomString;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandExec implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("wscraft")) {
            if (args.length < 3) {
                if (args[0].equalsIgnoreCase("token")) {
                    if (commandSender instanceof Player) {
                        Player player = (Player) commandSender;
                        if (args[1].equalsIgnoreCase("create")) {
                            if (DataBase.getToken(player) != null) {
                                String Token = RandomString.getRandomString(9);
                                DataBase.insert(player.getName(), Token);
                                player.sendMessage("Token生成成功：" + Token);
                                return true;
                            }
                            player.sendMessage("你已经拥有一个Token了");
                            player.sendMessage("输入 /wscraft token view 来查看你的Token");
                            return true;
                        }
                        if (args[1].equalsIgnoreCase("view")) {
                            String Token = DataBase.getToken(player);
                            if (Token == null) {
                                player.sendMessage("你还没有生成Token");
                                player.sendMessage("输入 /wscraft token create 来生成一个Token");
                                return true;
                            }
                            player.sendMessage("你的Token为：" + Token);
                            return true;
                        }
                    }
                    commandSender.sendMessage("仅玩家才可执行此命令");
                }
            }
        }
        return false;
    }
}
