package top.echovoid.service.user.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import top.echovoid.service.user.entity.UserAccount;

import java.util.Date;
import java.util.List;

/**
 * 用户详细信息
 *
 * @author Peng
 * @since 2025/4/13
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserDetailInfoDTO extends UserinfoDTO {
    private Date createdAt;
    private Date updatedAt;
    private Boolean active;
    private List<UserAccount> loginAccountList;
}