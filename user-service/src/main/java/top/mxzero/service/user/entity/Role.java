package top.mxzero.service.user.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author Peng
 * @since 2025/2/5
 */
@Data
@TableName("t_role")
public class Role {
    @TableId
    private Long id;
    private String name;
}