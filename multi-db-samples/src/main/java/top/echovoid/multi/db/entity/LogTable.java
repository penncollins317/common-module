package top.echovoid.multi.db.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @author Penn Collins
 * @since 2025/4/25
 */

@Data
@TableName("t_log")
public class LogTable {
    private Long id;
    private String text;
    private Date createdAt;
    private Long tenantId;
}
