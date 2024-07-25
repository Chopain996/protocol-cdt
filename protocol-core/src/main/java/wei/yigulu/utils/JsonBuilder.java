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

}

