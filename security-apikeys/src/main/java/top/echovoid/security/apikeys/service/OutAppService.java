package top.echovoid.security.apikeys.service;

import top.echovoid.security.apikeys.dto.request.CreateOutAppRequestDTO;
import top.echovoid.security.apikeys.dto.response.AppInfoDTO;
import top.echovoid.security.apikeys.dto.response.OutAppDTO;

import java.util.List;

/**
 * @author Penn Collins
 * @since 2025/2/15
 */
public interface OutAppService {

    /**
     * 新建应用
     */
    Long createApp(CreateOutAppRequestDTO dto);

    /**
     * 获取应用信息
     */
    AppInfoDTO getAppInfo(Long appId, Long userId);

    /**
     * 重置应用密钥
     */
    String resetSecretKey(Long appId, Long userId);

    /**
     * 设置应用对称密钥
     */
    boolean updateAesKey(Long appId, Long userId, String aesKey);

    /**
     * 应用列表
     */
    List<OutAppDTO> listApp(Long userId);

    /**
     * 关闭应用
     */
    boolean disableApp(Long appId, Long userId);
}
