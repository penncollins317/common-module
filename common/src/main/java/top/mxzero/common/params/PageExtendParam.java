package top.mxzero.common.params;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Set;

/**
 * @author Peng
 * @email qianmeng6879@163.com
 * @since 2024/2/21
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PageExtendParam extends PageParam implements Serializable {
    private Set<String> orders;
    public Set<String> fields;
}