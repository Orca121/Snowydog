package com.orcaswater.snowydog.engine;

import jakarta.servlet.*;

import java.io.IOException;

/**
 * @projectName: snowydog
 * @package: com.orcaswater.snowydog.engine
 * @className: FilterChainImpl
 * @author: Orca121
 * @description: FilterChainImpl
 * @createTime: 2024-05-07 16:33
 * @version: 1.0
 */

public class FilterChainImpl implements FilterChain {
    final Filter[] filters;
    final Servlet servlet;
    // Filter总数量
    final int total;
    // 下一个要处理的Filter[index]
    int index = 0;

    public FilterChainImpl(Filter[] filters, Servlet servlet) {
        this.filters = filters;
        this.servlet = servlet;
        this.total = filters.length;
    }
    /**
     * @param request:
     * @param response:
     * @return void
     * @author: orca121
     * @description: 注意FilterChain是一个递归调用，
     * 因为在执行Filter.doFilter()时，需要把FilterChain自身传进去，在
     * 执行Filter.doFilter()之前，就要把index调整到正确的值。
     * @createTime: 2024/5/7 16:35
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response) throws IOException, ServletException {
        if (index < total) {
            int current = index;
            index++;
            // 调用下一个Filter处理:
            filters[current].doFilter(request, response, this);
        } else {
            // 调用Servlet处理:
            servlet.service(request, response);
        }
    }
}
