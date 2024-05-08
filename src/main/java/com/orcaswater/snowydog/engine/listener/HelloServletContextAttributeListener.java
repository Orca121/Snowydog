package com.orcaswater.snowydog.engine.listener;

import jakarta.servlet.ServletContextAttributeEvent;
import jakarta.servlet.ServletContextAttributeListener;
import jakarta.servlet.annotation.WebListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @projectName: snowydog
 * @package: com.orcaswater.snowydog.engine.listener
 * @className: HelloServletContextAttributeListener
 * @author: Orca121
 * @description: 用于监听ServletContext属性的添加、修改和删除事件；
 * @createTime: 2024-05-07 22:13
 * @version: 1.0
 */
@Deprecated
@WebListener
public class HelloServletContextAttributeListener implements ServletContextAttributeListener {

    final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void attributeAdded(ServletContextAttributeEvent event) {
        logger.info(">>> ServletContext attribute added: {} = {}", event.getName(), event.getValue());
    }

    @Override
    public void attributeRemoved(ServletContextAttributeEvent event) {
        logger.info(">>> ServletContext attribute removed: {} = {}", event.getName(), event.getValue());
    }

    @Override
    public void attributeReplaced(ServletContextAttributeEvent event) {
        logger.info(">>> ServletContext attribute replaced: {} = {}", event.getName(), event.getValue());
    }
}
