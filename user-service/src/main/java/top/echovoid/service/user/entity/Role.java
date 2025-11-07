package top.echovoid.service.user.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author Penn Collins
 * @since 2025/2/5
 */
@Data
@TableName("t_role")
public class Role {
    @TableId
    private Long id;
    private String name;
}