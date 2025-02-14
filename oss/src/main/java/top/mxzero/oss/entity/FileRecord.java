package top.mxzero.oss.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Peng
 * @since 2024/10/19
 */
@Data
@TableName("file_record")
public class FileRecord implements Serializable, Cloneable {
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    private String name;
    private String url;
    private String path;
    private String contentType;
    private Long size;
    private String origin;
    private Long userId;
    private Long relatedId;
    private Date createdAt;
    private Date updatedAt;
    private String hash;
    @TableLogic
    private Integer isDeleted;

    @Override
    public FileRecord clone() {
        try {
            FileRecord clone = (FileRecord) super.clone();
            clone.setId(null);
            clone.setHash(null);
            // TODO: copy mutable state here, so the clone can't change the internals of the original
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}