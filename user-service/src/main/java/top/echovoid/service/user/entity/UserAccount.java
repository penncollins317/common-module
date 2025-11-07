package top.echovoid.service.user.entity;

import lombok.Data;
import top.echovoid.service.user.enums.AccountType;

import java.util.Date;

/**
 * @author Penn Collins
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