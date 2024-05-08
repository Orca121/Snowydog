package com.orcaswater.snowydog.engine.listener;

import jakarta.servlet.ServletRequestAttributeEvent;
import jakarta.servlet.ServletRequestAttributeListener;
import jakarta.servlet.annotation.WebListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @projectName: snowydog
 * @package: com.orcaswater.snowydog.engine.listener
 * @className: HelloServletRequestAttributeListener
 * @author: Orca121
 * @description: 用于监听ServletRequest属性的添加、修改和删除事件。
 * @createTime: 2024-05-07 22:14
 * @version: 1.0
 */
@Deprecated
@WebListener
public class HelloServletRequestAttributeListener implements ServletRequestAttributeListener {

    final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void attributeAdded(ServletRequestAttributeEvent srae) {
        logger.info(">>> ServletRequest attribute added: {} = {}", srae.getName(), srae.getValue());
    }

    @Override
    public void attributeRemoved(ServletRequestAttributeEvent srae) {
        logger.info(">>> ServletRequest attribute removed: {} = {}", srae.getName(), srae.getValue());
    }

    @Override
    public void attributeReplaced(ServletRequestAttributeEvent srae) {
        logger.info(">>> ServletRequest attribute replaced: {} = {}", srae.getName(), srae.getValue());
    }
}
