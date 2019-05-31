package com.cm.common;

import com.google.common.collect.Maps;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class JsonResult {

    private boolean ret;

    private String msg;

    private Object data;

    private JsonResult(boolean ret) {
        this.ret = ret;
    }

    public static JsonResult success() {
        return new JsonResult(true);
    }

    public static JsonResult success(String msg) {
        final JsonResult jsonResult = new JsonResult(true);
        jsonResult.setMsg(msg);
        return jsonResult;
    }

    public static <T> JsonResult success(T data) {
        final JsonResult jsonResult = new JsonResult(true);
        jsonResult.setData(data);
        return jsonResult;
    }

    public static <T> JsonResult success(String msg, T data) {
        final JsonResult jsonResult = new JsonResult(true);
        jsonResult.setMsg(msg);
        jsonResult.setData(data);
        return jsonResult;
    }

    public static JsonResult fail(String msg) {
        final JsonResult jsonResult = new JsonResult(false);
        jsonResult.setMsg(msg);
        return jsonResult;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> map = Maps.newHashMap();
        map.put("ret", ret);
        map.put("msg", msg);
        map.put("data", data);
        return map;
    }
}
