package com.orcaswater.snowydog.utils;

/**
 * @projectName: snowydog
 * @package: com.orcaswater.snowydog.utils
 * @className: HtmlUtils
 * @author: Orca121
 * @description:
 * @createTime: 2024-05-07 23:02
 * @version: 1.0
 */

public class HtmlUtils {

    public static String encodeHtml(String s) {
        return s.replace("<", "&lt;").replace(">", "&gt;").replace("&", "&amp;").replace("\"", "&quot;");
    }
}
