package com.orcaswater.snowydog;

import com.orcaswater.snowydog.connector.HttpConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @projectName: snowydog
 * @package: com.orcaswater.snowydog
 * @className: Start
 * @author: Orca121
 * @description: start-up for simple servlet server
 * @createTime: 2024-05-07 11:22
 * @version: 1.0
 */

public class Start {
    static Logger logger = LoggerFactory.getLogger(Start.class);

    public static void main(String[] args) throws Exception {
        try (HttpConnector connector = new HttpConnector()) {
            for (;;) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    break;
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        logger.info("jerrymouse http server was shutdown.");
    }
}
