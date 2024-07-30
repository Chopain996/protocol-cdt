package wei.yigulu.utils;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ConfigRead {

    public static Map<String, Object> configMap = new HashMap<>();
    public static final String CONFIG_FILE_PATH="protocol-core/src/main/resources/config.json";

    public ConfigRead() {
        readConfig();
    }

    public Map<String, Object> readConfig() {
        FileInputStream fileInputStream = null;
        Scanner scanner = null;
        try {
            //protocol-core/src/main/java/wei/yigulu/utils/ConfigRead.java
            //protocol-core/src/main/resources/config.json
            //../../../../../resources/config.json
            File file = new File(CONFIG_FILE_PATH);
            if (file.exists()) { // 确保文件存在
                fileInputStream = new FileInputStream(file);
                scanner = new Scanner(fileInputStream, "UTF-8").useDelimiter("\\A");
                String content = scanner.next(); // 读取文件内容到字符串
                JSONObject jsonObject = new JSONObject(content); // 使用字符串创建JSONObject
                jsonObject.keys().forEachRemaining(key -> {
                    configMap.put(key, jsonObject.get(key));
                });
            } else {
                System.err.println("Error: File does not exist - " + file.getPath());
            }
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error parsing the JSON file: " + e.getMessage());
        } finally {
            if (scanner != null) {
                scanner.close();
            }
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    System.err.println("Error closing the file: " + e.getMessage());
                }
            }
        }

        return configMap;
    }
}
