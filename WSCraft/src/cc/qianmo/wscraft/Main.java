package cc.qianmo.wscraft;

import cc.qianmo.wscraft.Player.CommandExec;
import cc.qianmo.wscraft.Player.PlayerEvent;
import cc.qianmo.wscraft.Utils.DataBase;
import cc.qianmo.wscraft.Utils.Http;
import cc.qianmo.wscraft.WebSocket.Server;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.Objects;

//作者：千沫qianmo
//这是WSCraft的主类
//Version:2.1.2


public class Main extends JavaPlugin {
    private static Main main;
    long startTime;
    long endTime;
    long loadTime;

    //当插件被启动时，启动WebSocket服务
    @Override
    public void onLoad() {
        startTime = System.currentTimeMillis();
        getLogger().info("WSCraft正在启动...");
        getLogger().info("版本:2.1.2");
        main = this;
        saveDefaultConfig();
        Service(true);
    }

    //当插件启动完毕时，检查更新等
    @Override
    public void onEnable() {
        getLogger().info("正在初始化数据库");
        DataBase.setup();
        Bukkit.getPluginManager().registerEvents(new PlayerEvent(), this);
        Objects.requireNonNull(Bukkit.getPluginCommand("wscraft")).setExecutor(new CommandExec());
        //updateChecker();
        endTime = System.currentTimeMillis();
        loadTime = (endTime - startTime)/1000;
        getLogger().info("WSCraft启动成功！耗时: " + loadTime + "s");
    }

    //当插件被卸载时，关闭WebSocket服务
    @Override
    public void onDisable() {
        getLogger().info("WSCraft正在卸载！");
        Service(false);
    }


    /**
     * @param action true为开false为关
     */
    private void Service(Boolean action) {
        FileConfiguration config = getConfig();
        Server s = new Server(config.getInt("PORT"));
        if (action) {
            s.start();
        } else {
            try {
                s.stop();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    //检查更新
    private void updateChecker() {
        if (!getConfig().getBoolean("UPDATE_CHECK")) {
            getLogger().warning("更新检查已禁用！");
            getLogger().warning("强烈建议开启更新检查以及时获取更新的版本");
            return;
        }
        getLogger().info("正在检查更新");
        String result = Http.sendGet("http://update.mckits.fun", "plugin=WSCraft&version=2.1.0");
        if (result.equalsIgnoreCase("true")) {
            this.getLogger().info("该插件版本为2.1.2,已经是最新版本！");
        } else {
            this.getLogger().info("检测到更新版本，请到 https://github.com/qianmo2233/WSCraft2/releases 下载");
        }
    }


    /**
     * @return 插件主类
     */
    public static Main getInstance() {
        return main;
    }
}
