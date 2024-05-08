package com.orcaswater.snowydog.engine.mapping;

import jakarta.servlet.Filter;

/**
 * @projectName: snowydog
 * @package: com.orcaswater.snowydog.engine.mapping
 * @className: FilterMapping
 * @author: Orca121
 * @description: FilterMapping
 * @createTime: 2024-05-07 16:20
 * @version: 1.0
 */

public class FilterMapping extends AbstractMapping{
    public final String filterName;
    public final Filter filter;

    public FilterMapping(String filterName, String urlPattern, Filter filter) {
        // 编译为正则表达式
        super(urlPattern); // 编译为正则表达式
        this.filterName = filterName;
        this.filter = filter;
    }
}
