package top.mxzero.security.core.service;

import top.mxzero.security.core.dto.UserinfoDTO;
import top.mxzero.security.core.dto.UsernamePasswordArgs;

import java.util.List;

/**
 * @author Peng
 * @since 2025/1/21
 */
public interface UserService {

    /**
     * 获取用户基础信息
     *
     * @param userId 用户ID
     * @return 用户基础信息DTO
     */
    UserinfoDTO getUserinfo(Long userId);

    /**
     * 批量获取用户基础信息
     *
     * @param userIds 用户ID列表
     * @return 用户基础信息DTO列表
     */
    List<UserinfoDTO> getUserinfo(List<Long> userIds);

    /**
     * 新增用户
     *
     * @param args 用户名和密码参数
     * @return 用户ID
     */
    Long addUser(UsernamePasswordArgs args);
}
