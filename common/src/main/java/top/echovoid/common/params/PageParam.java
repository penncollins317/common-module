package top.echovoid.common.params;

import lombok.Data;

/**
 * @author Peng
 * @since 2024/10/3
 */
@Data
public class PageParam {
    private long page = 1L;
    private long size = 10L;
    private String search;
}