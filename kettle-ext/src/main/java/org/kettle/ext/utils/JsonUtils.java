package org.kettle.ext.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

/**
 * @description: json工具
 * @author: ZX
 * @date: 2018/11/21 11:33
 */
public class JsonUtils {

    public static void success(String message) throws IOException {
        success("系统提示", message);
    }

    public static void success(String title, String message) throws IOException {
        response(true, title, message);
    }

    public static void fail(String message) throws IOException {
        fail("系统提示", message);
    }

    public static void fail(String title, String message) throws IOException {
        response(false, title, message);
    }

    public static void response(boolean success, String title, String message) throws IOException {
        JsonObject jsonObject = new JsonObject();
        jsonObject.put("success", success);
        jsonObject.put("title", title);
        jsonObject.put("message", message);

        response(jsonObject);
    }

    public static void response(JsonObject jsonObject) throws IOException {
        HttpServletResponse response = tl.get();
        response.setContentType("text/html; charset=utf-8");
        response.getWriter().write(jsonObject.toString());
    }

    public static void response(JsonArray jsonArray) throws IOException {
        HttpServletResponse response = tl.get();
        response.setContentType("text/html; charset=utf-8");
        response.getWriter().write(jsonArray.toString());
    }

    public static void responseXml(String xml) throws IOException {
        HttpServletResponse response = tl.get();
        response.setContentType("text/xml; charset=utf-8");
        response.getWriter().write(xml);
    }

    public static void download(File file) throws IOException {
        HttpServletResponse response = tl.get();
        response.setContentType("multipart/form-data");
        response.setHeader("Content-Disposition", "attachment;fileName=" + file.getName());

        InputStream is = null;
        try {
            is = FileUtils.openInputStream(file);

            IOUtils.copy(is, response.getOutputStream());
        } finally {
            IOUtils.closeQuietly(is);
        }

    }

    private static ThreadLocal<HttpServletResponse> tl = new ThreadLocal<HttpServletResponse>();

    public static void putResponse(HttpServletResponse response) {
        tl.set(response);
    }

}
