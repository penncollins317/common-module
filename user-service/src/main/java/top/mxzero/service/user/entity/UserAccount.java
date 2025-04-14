package top.mxzero.service.user.entity;

import lombok.Data;
import top.mxzero.service.user.enums.AccountType;

import java.util.Date;

/**
 * @author Peng
 * @since 2025/4/13
 */
@Data
public class UserAccount {
    private Long id;
    private Long userId;
    private AccountType accountType;
    private String accountValue;
    private Boolean validated;
    private Date createdAt;
}