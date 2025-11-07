package top.echovoid.common.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 游标分页结果集
 *
 * @author Peng
 * @email penncollins317@gmail.com
 * @since 2023/12/4
 */
@Data
public class CursorPageDTO<T> implements Serializable {
    /**
     * 分页数据
     */
    private List<T> records;

    /**
     * 最后一条记录ID
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long lastId;

    /**
     * 当前数据量
     */
    private int size;

    /**
     * 数据总行数
     */
    private long total;
}
