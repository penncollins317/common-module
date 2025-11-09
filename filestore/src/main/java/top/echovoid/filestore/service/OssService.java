package top.echovoid.filestore.service;

import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.NotNull;
import top.echovoid.filestore.dto.OssUploadResult;

import java.io.InputStream;
import java.util.List;

/**
 * @author Penn Collins
 * @email penncollins317@gmail.com
 * @since 2023/8/19
 */
public interface OssService {
    /**
     * 字节上传
     *
     * @param data
     * @param filename
     * @param contentType
     * @return
     */
    OssUploadResult upload(byte[] data, String filename, String contentType);

    /**
     * 输入流上传
     *
     * @param inputStream
     * @param filename
     * @param contentType
     * @param size
     * @return
     */
    OssUploadResult upload(InputStream inputStream, String filename, String contentType, long size);

    /**
     * 判单文件是否存在
     *
     * @param filename
     * @return
     */
    boolean exists(String filename);

    /**
     * 删除文件
     *
     * @param filename
     * @return
     */
    boolean remove(String filename);


    /**
     * 批量删除文件
     *
     * @param objectNames
     * @return
     */
    default long removeBatch(@NotNull List<String> objectNames) {
        List<Boolean> list = objectNames.stream().map(this::remove).toList();
        return list.size();
    }


    /**
     * 预上传签名
     *
     * @param name
     * @return
     */
    String prepareSign(String name);

    /**
     * 生成私有访问链接
     *
     * @param key 文件key
     * @return
     */
    default String privateAccessUrl(String key) {
        throw new NotImplementedException("method not implement.");
    }

    /**
     * 获取文件URL前缀
     *
     * @return
     */
    default String prefixName() {
        return "";
    }
}