package com.orcaswater.snowydog.engine.mapping;

import jakarta.servlet.Servlet;

/**
 * @projectName: snowydog
 * @package: com.orcaswater.snowydog.engine.mapping
 * @className: ServletMapping
 * @author: Orca121
 * @description: a mapping route for servlet
 * @createTime: 2024-05-07 11:54
 * @version: 1.0
 */

public class ServletMapping extends AbstractMapping{
    public final Servlet servlet;

    public ServletMapping(String urlPattern, Servlet servlet) {
        super(urlPattern);
        this.servlet = servlet;
    }
}
