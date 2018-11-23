package org.kettle.ext.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.util.StringUtils;

/**
 * @description: JSON数组工具
 *
 * @author:   ZX
 * @date:     2018/11/21 11:30
 */
public class JsonArray extends ArrayList<Object> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public JsonArray() {

    }

    public JsonArray(int initialCapacity) {
        super(initialCapacity);
    }

    public JsonArray(List<Object> list) {
        if (list != null) {
            this.addAll(list);
        }
    }

    public static JsonArray fromObject(String json) throws IOException {
        JsonArray jsonArray = new JsonArray();
        if (!StringUtils.hasText(json)) {
            return jsonArray;
        }

        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, JsonArray.class);
    }

    public static JsonArray fromObject(List list) {
        return new JsonArray(list);
    }

    public String getString(int i) {
        return (String) get(i);
    }

    public JsonObject getJSONObject(int i) {
        Map<String, Object> m = (Map<String, Object>) get(i);
        return new JsonObject(m);
    }

    @Override
    public String toString() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
