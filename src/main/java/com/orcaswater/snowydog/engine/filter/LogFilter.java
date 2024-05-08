package com.orcaswater.snowydog.engine.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @projectName: snowydog
 * @package: com.orcaswater.snowydog.engine.filter
 * @className: LogFilter
 * @author: Orca121
 * @description: 匹配/*，打印请求方法、路径等信息
 * @createTime: 2024-05-07 16:36
 * @version: 1.0
 */
@Deprecated
@WebFilter(urlPatterns = "/*")
public class LogFilter implements Filter {

    final Logger logger = LoggerFactory.getLogger(getClass());

    // Filter接口中init方法和destroy方法是default方法，非必须实现
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        logger.info("{}: {}", req.getMethod(), req.getRequestURI());
        chain.doFilter(request, response);
    }


}
