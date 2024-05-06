package com.orcaswater.snowydog;


import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;


/**
 * @projectName: Snowydog
 * @package: com.orcaswater.snowydog
 * @className: SimpleHttpServer
 * @author: Orca121
 * @description: a simple demo for http server
 * @createTime: 2024-05-06 22:21
 * @version: 1.0
 */

public class SimpleHttpServer implements HttpHandler, AutoCloseable{

    final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * @description: 启动和运行HTTP服务器
     * @createTime: 2024/5/6 22:37
     */
    public static void main(String[] args) {
        String host = "0.0.0.0";
        int port = 8080;
        //已实现AutoCloseable接口，自动释放资源
        try (SimpleHttpServer connector = new SimpleHttpServer(host, port)) {
            //创建一个简单的HTTP服务器，并让它持续运行。服务器本身没有提供任何服务或响应，它只是简单地保持运行状态。
            for (;;) {
                try {
                    //Thread.sleep(1000)调用允许服务器在循环中休眠，减少CPU的使用率，而不会立即响应任何HTTP请求。
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**直接使用JDK内置的jdk.httpserver。
     * jdk.httpserver从JDK 9开始作为一个公开模块可以直接使用，
     * 它的包是com.sun.net.httpserver，主要提供以下几个类：
     * HttpServer：通过指定IP地址和端口号，定义一个HTTP服务实例；
     * HttpHandler：处理HTTP请求的核心接口，必须实现handle(HttpExchange)方法；
     * HttpExchange：可以读取HTTP请求的输入，并将HTTP响应输出给它。
     */
    final HttpServer httpServer;
    final String host;
    final int port;

    public SimpleHttpServer(String host, int port) throws IOException {
        this.host = host;
        this.port = port;
        this.httpServer = HttpServer.create(new InetSocketAddress("0.0.0.0", 8080), 0, "/", this);
        this.httpServer.start();
        logger.info("start snowydog http server at {}:{}", host, port);
    }

    @Override
    public void close() {
        this.httpServer.stop(3);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        URI uri = exchange.getRequestURI();
        String path = uri.getPath();
        String query = uri.getRawQuery();
        logger.info("{}: {}?{}", method, path, query);
        Headers respHeaders = exchange.getResponseHeaders();
        respHeaders.set("Content-Type", "text/html; charset=utf-8");
        respHeaders.set("Cache-Control", "no-cache");
        // 设置200响应:
        exchange.sendResponseHeaders(200, 0);
        String s = "<h1>Hello, world.</h1><p>" + LocalDateTime.now().withNano(0) + "</p>";
        try (OutputStream out = exchange.getResponseBody()) {
            out.write(s.getBytes(StandardCharsets.UTF_8));
        }
    }
}
