package com.orcaswater.snowydog.connector;

import com.orcaswater.snowydog.Config;
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
import org.slf4j.LoggerFactory;
import com.orcaswater.snowydog.engine.listener.HelloHttpSessionAttributeListener;
import com.orcaswater.snowydog.engine.listener.HelloHttpSessionListener;
import com.orcaswater.snowydog.engine.listener.HelloServletContextListener;
import com.orcaswater.snowydog.engine.listener.HelloServletContextAttributeListener;
import com.orcaswater.snowydog.engine.listener.HelloServletRequestAttributeListener;
import com.orcaswater.snowydog.engine.listener.HelloServletRequestListener;


import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.util.EventListener;
import java.util.List;
import java.util.concurrent.Executor;

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

    final Config config;
    final ClassLoader classLoader;
    final ServletContextImpl servletContext;
    final HttpServer httpServer;
    final Duration stopDelay = Duration.ofSeconds(5);


    public HttpConnector(Config config, String webRoot, Executor executor, ClassLoader classLoader, List<Class<?>> autoScannedClasses) throws IOException {
        logger.info("starting snowydog http server at {}:{}...", config.server.host, config.server.port);
        this.config = config;
        // 创建类加载器
        this.classLoader = classLoader;

        // 创建上下文类加载器
        Thread.currentThread().setContextClassLoader(this.classLoader);
        // 创建ServletContext:
        ServletContextImpl ctx = new ServletContextImpl(classLoader, config, webRoot);
        ctx.initialize(autoScannedClasses);
        this.servletContext = ctx;
        // 恢复默认的线程的上下文类加载器:
        Thread.currentThread().setContextClassLoader(null);

        // 开启服务器
        this.httpServer = HttpServer.create(new InetSocketAddress(config.server.host, config.server.port), config.server.backlog, "/", this);
        this.httpServer.setExecutor(executor);
        this.httpServer.start();
        logger.info("snowydog http server started at {}:{}...", config.server.host, config.server.port);
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
        var response = new HttpServletResponseImpl(this.config, adapter);
        // 创建Request时，需要引用servletContext和response:
        var request = new HttpServletRequestImpl(this.config, this.servletContext, adapter, response);
        // process:
        try {
            Thread.currentThread().setContextClassLoader(this.classLoader);
            this.servletContext.process(request, response);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            // 恢复默认的线程的上下文类加载器:
            Thread.currentThread().setContextClassLoader(null);
            // 如果发现没有发送Header，则需要立刻发送Header，否则浏览器无法收到响应。
            response.cleanup();
        }
    }


    @Override
    public void close() {
        this.servletContext.destroy();
        this.httpServer.stop((int) this.stopDelay.toSeconds());
    }
}
