package cc.qianmo.wscraft.Utils;

import cc.qianmo.wscraft.Main;
import org.bukkit.entity.Player;

import java.sql.*;

public class DataBase {

    static String Path = Main.getInstance().getDataFolder().getPath() + "/Data.db";
    static Connection c;
    static ResultSet resultSet = null;
    static PreparedStatement ps = null;

    public static Connection getConn() {
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:" + Path);
        } catch (ClassNotFoundException | SQLException e) {
            Main.getInstance().getLogger().warning("未发现sqlite驱动！");
            e.printStackTrace();
            Main.getInstance().getPluginLoader().disablePlugin(Main.getInstance());
        }
        return c;
    }

    public static void setup() {
        Connection conn = getConn();
        try {
            conn.createStatement().execute("CREATE TABLE IF NOT EXISTS playerData(Player varchar(20),Token varchar(20))");
        } catch (SQLException e) {
            Main.getInstance().getLogger().warning("初始化数据库出错！");
            e.printStackTrace();
            Main.getInstance().getPluginLoader().disablePlugin(Main.getInstance());
        } finally {
            Close(conn);
        }
    }

    public static boolean insert(String Player, String Token) {
        String sql = "INSERT INTO playerData (Player, Token) VALUES ('" + Player +"' , '" + Token + "')";
        Connection conn = getConn();
        try {
            return conn.createStatement().execute(sql);
        } catch (SQLException e) {
            Main.getInstance().getLogger().warning("数据库查询失败!");
            e.printStackTrace();
            return false;
        } finally {
            Close(conn);
        }
    }

    public static boolean isExists(String token) {
        String sql = "SELECT count(Player) count FROM playerData WHERE Token = '" + token + "'";
        Connection conn = getConn();
        try {
            ResultSet resultSet = conn.createStatement().executeQuery(sql);
            int count = resultSet.getInt("count");
            return count != 0;
        } catch (SQLException e) {
            Main.getInstance().getLogger().warning("数据库查询失败!");
            e.printStackTrace();
            return false;
        } finally {
            Close(conn);
        }
    }

    public static boolean isExists(Player Player) {
        String player = Player.getName();
        String sql = "SELECT count(Player) count FROM playerData WHERE Player = ?";
        Connection conn = getConn();
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, player);
            resultSet = ps.executeQuery();
            int count = resultSet.getInt("count");
            return count != 0;
        } catch (SQLException e) {
            Main.getInstance().getLogger().warning("数据库查询失败!");
            e.printStackTrace();
            return false;
        } finally {
            Close(conn, resultSet, ps);
        }
    }

    public static boolean update(String Player, String Token) {
        String sql = "UPDATE playerData SET Token = '" + Token + "' WHERE Player = '" + Player + "'";
        Connection conn = getConn();
        try {
           return conn.createStatement().execute(sql);
        } catch (SQLException e) {
            Main.getInstance().getLogger().warning("数据库查询失败!");
            e.printStackTrace();
            return false;
        } finally {
            Close(conn);
        }
    }

    public static String getToken(Player player) {
        String sql = "SELECT Token FROM playerData WHERE Player = ?";
        Connection conn = getConn();
        if (isExists(player)) {
            try {
                ps = conn.prepareStatement(sql);
                ps.setString(1, player.getName());
                resultSet = ps.executeQuery();
                return resultSet.getString("Token");
            } catch (SQLException e) {
                Main.getInstance().getLogger().warning("数据库查询失败!");
                e.printStackTrace();
                return null;
            } finally {
                Close(conn, resultSet, ps);
            }
        }
        return null;
    }

    public static String getPlayer(String token) {
        String sql = "SELECT Player FROM playerData WHERE Token = ?";
        Connection conn = getConn();
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, token);
            resultSet = ps.executeQuery();
            return resultSet.getString("Player");
        } catch (SQLException e) {
            Main.getInstance().getLogger().warning("数据库查询失败!");
            e.printStackTrace();
            return null;
        } finally {
            Close(conn, resultSet, ps);
        }
    }

    public static void Close(Connection conn, ResultSet resultSet, PreparedStatement ps) {
        try {
            resultSet.close();
            ps.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void Close(Connection conn) {
        try {
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
