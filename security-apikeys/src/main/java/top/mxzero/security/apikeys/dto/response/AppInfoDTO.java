package top.mxzero.security.apikeys.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author Peng
 * @since 2025/2/15
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AppInfoDTO extends OutAppDTO{
    private String aesKey;
    private List<String> whiteList;
}