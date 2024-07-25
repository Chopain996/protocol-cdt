package wei.yigulu.utils;

import com.alibaba.fastjson.JSONArray;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class JsonBuilder {

    private static ObjectMapper mapper = new ObjectMapper();

    public static String JsonToString(Map map) throws JsonProcessingException {
        // 将Map转换为JSON字符串
        JSONArray jarray = new JSONArray();
        jarray.add(map);

        String str =jarray.toString();
        return str;

    }

    public static void main(String[] args) throws JsonProcessingException {


        // 基础字段
        Map<String, Object> jsonMap = new LinkedHashMap<>();
        jsonMap.put("数据类型", "重要遥测");
        jsonMap.put("源地址", 11);
        jsonMap.put("目标地址", 88);

        // 嵌套的JSON对象
        Map<String, String> datas = new HashMap<>();
        datas.put("0", "false");
        datas.put("1", "true");
        datas.put("2", "false");

        // 将嵌套的JSON对象添加到主JSON对象中
        jsonMap.put("datas", datas);

        // 将Map转换为JSON字符串


        System.out.println(JsonToString(jsonMap));
    }
}

