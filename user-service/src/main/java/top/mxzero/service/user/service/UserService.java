package top.mxzero.service.user.service;

import jakarta.annotation.Nullable;
import top.mxzero.common.dto.PageDTO;
import top.mxzero.common.params.PageSearchParam;
import top.mxzero.service.user.dto.*;

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
    @Nullable
    UserinfoDTO getUserinfo(Long userId);

    /**
     * 通过用户名获取用户信息
     *
     * @param username 用户名
     * @return 用户基础信息DTO
     */
    @Nullable
    UserinfoDTO getUserinfoByUsername(String username);

    /**
     * 获取用户详细信息
     *
     * @param userId 用户ID
     * @return 用户详细信息，当用户ID不存在时返回null
     */
    @Nullable
    UserDetailInfoDTO getUserDetailInfo(Long userId);

    /**
     * 批量获取用户基础信息
     *
     * @param userIds 用户ID列表
     * @return 用户基础信息DTO列表
     */
    List<UserinfoDTO> getUserinfo(List<Long> userIds);

    /**
     * 用户搜索
     *
     * @param param 查询参数
     */
    PageDTO<UserinfoDTO> search(PageSearchParam param);

    /**
     * 新增用户
     *
     * @param args 用户名和密码参数
     * @return 用户ID
     */
    Long addUser(UsernamePasswordArgs args);

    /**
     * 需改用户昵称、头像
     */
    boolean updateUserinfo(UserinfoModifyDTO dto);

    /**
     * 用户账号绑定
     */
    boolean BindAccountDTO(BindAccountDTO dto);
}
