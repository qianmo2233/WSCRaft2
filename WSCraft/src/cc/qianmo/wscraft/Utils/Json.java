package cc.qianmo.wscraft.Utils;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.util.Map;

public class Json {

    public static boolean isJson(String s) {
        JsonElement jsonElement;
        try {
            jsonElement = new JsonParser().parse(s);
        } catch (Exception e) {
            return false;
        }
        if (jsonElement == null) {
            return false;
        }
        if (!jsonElement.isJsonObject()) {
            return false;
        }
        return true;
    }

    public static Map<String, String> toMap(String s) {
        if (isJson(s)) {
            Gson gson = new Gson();
            return gson.fromJson(s, new TypeToken<Map<String,String>>() {}.getType());
        }
        return null;
    }

    public static String toJson(Map map) {
        Gson gson = new Gson();
        return gson.toJson(map);
    }
}
