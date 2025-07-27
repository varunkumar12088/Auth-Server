package com.academy.auth.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.Map;

public class ObjectUtil {

    public static ObjectNode buildObjectNodeFromMap(Map<String, Object> map, ObjectMapper objectMapper){
        ObjectNode jsonNode = objectMapper.createObjectNode();
        for(Map.Entry<String, Object> entry : map.entrySet()){
            jsonNode.put(entry.getKey(), String.valueOf(entry.getValue()));
        }
        return jsonNode;
    }
}
