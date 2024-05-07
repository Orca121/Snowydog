package com.orcaswater.snowydog.connector;

import com.orcaswater.snowydog.engine.ServletContextImpl;
import com.orcaswater.snowydog.engine.servlet.HelloServlet;
import com.orcaswater.snowydog.engine.servlet.IndexServlet;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.orcaswater.snowydog.engine.HttpServletRequestImpl;
import com.orcaswater.snowydog.engine.HttpServletResponseImpl;
import com.sun.net.httpserver.HttpServer;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.util.List;

/**
 * @projectName: snowydog
 * @package: com.orcaswater.snowydog.connector
 * @className: HttpConnector
 * @author: Orca121
 * @description: a new connector with adapter pattern
 * @createTime: 2024-05-07 11:05
 * @version: 1.0
 */

public class HttpConnector implements HttpHandler, AutoCloseable {
    final Logger logger = LoggerFactory.getLogger(getClass());

    // 持有ServletContext实例:
    final ServletContextImpl servletContext;
    final HttpServer httpServer;

    public HttpConnector() throws IOException {
        // 创建ServletContext:
        this.servletContext = new ServletContextImpl();
        // 初始化Servlet:
        this.servletContext.initialize(List.of(IndexServlet.class, HelloServlet.class));

        String host = "0.0.0.0";
        int port = 8080;
        this.httpServer = HttpServer.create(new InetSocketAddress(host, port), 0, "/", this);
        this.httpServer.start();
        logger.info("snowydog http server started at {}:{}...", host, port);
    }

    /**
     * @param exchange:
     * @return void
     * @author: patri
     * @description: 创建三个实例，用适配器模式转换HttpExchange为Servlet标准
     * @createTime: 2024/5/7 11:30
     */
    @Override
    public void handle(HttpExchange exchange) throws IOException {
//        logger.info("{}: {}?{}", exchange.getRequestMethod(), exchange.getRequestURI().getPath(), exchange.getRequestURI().getRawQuery());
        var adapter = new HttpExchangeAdapter(exchange);
        var request = new HttpServletRequestImpl(adapter);
        var response = new HttpServletResponseImpl(adapter);
        // process:
        try {
//            process(request, response);
            this.servletContext.process(request, response);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * @param request:
     * @param response:
     * @return void
     * @author: patri
     * @description: 这个方法内部就可以按照Servlet标准来处理HTTP请求了，因为方法参数是标准的Servlet接口
     * @createTime: 2024/5/7 11:30
     */
    @Deprecated
    void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");
        String html = "<h1>Hello, " + (name == null ? "world" : name) + ".</h1>";
        response.setContentType("text/html");
        PrintWriter pw = response.getWriter();
        pw.write(html);
        pw.close();
    }

    @Override
    public void close() throws Exception {
        this.httpServer.stop(3);
    }
}
