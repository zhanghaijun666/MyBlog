package com.blog.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;

/**
 * HTTP 输出响应内容工具类
 */
public class ResponseUtils {

    /**
     * 发送HTTP响应信息
     *
     * @param response HTTP响应对象
     * @param message  信息内容
     * @throws IOException 抛出异常，由调用者捕获处理
     */
    public static void write(HttpServletResponse response, String message) throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter writer = response.getWriter();) {
            writer.write(message);
            writer.flush();
        }
    }

    /**
     * 发送HTTP响应信息,JSON格式
     *
     * @param response HTTP响应对象
     * @param message  输出对象
     * @throws IOException 抛出异常，由调用者捕获处理
     */
    public static void write(HttpServletResponse response, Object message) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        ObjectMapper mapper = new ObjectMapper();
        try (PrintWriter writer = response.getWriter();) {
            writer.write(mapper.writeValueAsString(message));
            writer.flush();
        }
    }

    /**
     * 下载文件
     *
     * @param response HTTP响应对象
     * @param message  输出对象
     * @throws IOException 抛出异常，由调用者捕获处理
     */
    public static void write(HttpServletResponse response, File file) throws IOException {
        String fileName = file.getName();
        try (OutputStream out = response.getOutputStream(); FileInputStream in = new FileInputStream(file);) {
            // 对文件名进行URL转义，防止中文乱码
            fileName = URLEncoder.encode(fileName, "UTF-8");
            // 空格用URLEncoder.encode转义后会变成"+"，所以要替换成"%20"，浏览器会解码回空格
            fileName = fileName.replace("+", "%20");
            // "+"用URLEncoder.encode转义后会变成"%2B"，所以要替换成"+"，浏览器不对"+"进行解码
            fileName = fileName.replace("%2B", "+");
            response.setContentType("application/x-msdownload;charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
            byte[] bytes = new byte[4096];
            int len = -1;
            while ((len = in.read(bytes)) != -1) {
                out.write(bytes, 0, len);
            }
            out.flush();
        }
    }

}
