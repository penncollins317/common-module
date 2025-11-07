package top.echovoid.common.params;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Set;

/**
 * @author Penn Collins
 * @email penncollins317@gmail.com
 * @since 2024/2/21
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PageExtendParam extends PageParam implements Serializable {
    private Set<String> orders;
    public Set<String> fields;
}