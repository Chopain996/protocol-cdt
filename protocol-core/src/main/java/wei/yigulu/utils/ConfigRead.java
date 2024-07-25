package wei.yigulu.utils;

import org.json.JSONObject;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public  class ConfigRead {

    private static final String FILE_PATH = "E:\\code\\protocol\\protocol-core\\src\\main\\resources\\config.json"; // 修改为实际路径
    public static Map<String, Object> configMap = new HashMap<>();

    public ConfigRead(){
        readConfig();
    }

    public static  Map<String, Object> readConfig() {
        try {
            Path path = Paths.get(FILE_PATH);
            if (Files.exists(path)) { // 确保文件存在
                String content = new String(Files.readAllBytes(path)); // 读取文件内容到字符串
                JSONObject jsonObject = new JSONObject(content); // 使用字符串创建JSONObject
                jsonObject.keys().forEachRemaining(key -> {
                    configMap.put(key, jsonObject.get(key));
                });
            } else {
                System.err.println("Error: File does not exist - " + FILE_PATH);
            }
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error parsing the JSON file: " + e.getMessage());
        }

        return configMap;
    }


}
