package com.orcaswater.snowydog.connector;

import com.orcaswater.snowydog.engine.ServletContextImpl;
import com.orcaswater.snowydog.engine.filter.LogFilter;
import com.orcaswater.snowydog.engine.filter.HelloFilter;
import com.orcaswater.snowydog.engine.servlet.HelloServlet;
import com.orcaswater.snowydog.engine.servlet.IndexServlet;
import com.orcaswater.snowydog.engine.servlet.LoginServlet;
import com.orcaswater.snowydog.engine.servlet.LogoutServlet;
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
import java.time.Duration;
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

    final Duration stopDelay = Duration.ofSeconds(5);

    public HttpConnector() throws IOException {
        // 创建ServletContext:
        this.servletContext = new ServletContextImpl();
        // 初始化Servlet(目前还是硬编码加载Servlet):
        this.servletContext.initServlets(List.of(IndexServlet.class, LoginServlet.class, LogoutServlet.class));
        // 初始化Filter
        this.servletContext.initFilters(List.of(LogFilter.class));
        // 开启服务器
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
        var adapter = new HttpExchangeAdapter(exchange);
        var response = new HttpServletResponseImpl(adapter);
        // 创建Request时，需要引用servletContext和response:
        var request = new HttpServletRequestImpl(this.servletContext, adapter, response);
        // process:
        try {
            this.servletContext.process(request, response);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }finally {
            response.cleanup();
        }
    }


    @Override
    public void close() throws Exception {
        this.httpServer.stop((int) this.stopDelay.toSeconds());
    }
}
