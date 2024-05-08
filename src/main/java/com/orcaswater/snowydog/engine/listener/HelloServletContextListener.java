package com.orcaswater.snowydog.engine.listener;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @projectName: snowydog
 * @package: com.orcaswater.snowydog.engine.listener
 * @className: HelloServletContextListener
 * @author: Orca121
 * @description: 用于监听ServletContext的创建和销毁事件；
 * @createTime: 2024-05-07 22:14
 * @version: 1.0
 */
@Deprecated

@WebListener
public class HelloServletContextListener implements ServletContextListener {

    final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        logger.info(">>> ServletContext initialized: {}", sce.getServletContext());
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        logger.info(">>> ServletContext destroyed: {}", sce.getServletContext());
    }
}
