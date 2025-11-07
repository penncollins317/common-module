package top.echovoid.common.utils;

import org.springframework.web.multipart.MultipartFile;
import top.echovoid.common.exceptions.ServiceException;

/**
 * 文件处理工具类
 *
 * @author Peng
 * @since 2025/4/23
 */
public class FileUtils {

    private FileUtils() {
    }

    /**
     * 检查文件是否正确
     *
     * @param file 上传文件对象
     */
    public static void checkFile(MultipartFile file) {
        if (file.getOriginalFilename() == null) {
            throw new ServiceException("The file is missing a name.");
        }
        if (file.getOriginalFilename().length() > 255) {
            throw new ServiceException("The maximum character length of the file name is 255.");
        }
        String contentType = file.getContentType();
        if (contentType == null) {
            throw new ServiceException("The file is missing \"content_type\".");
        }
    }

    /**
     * 获取文件扩展名
     *
     * @param filename 文件原始名称
     * @return 扩展名
     */
    public static String getExtendName(String filename) {
        if (filename == null || filename.trim().isEmpty()) {
            return "";
        }

        int lastDotIndex = filename.lastIndexOf('.');
        if (lastDotIndex == -1 || lastDotIndex == 0 || lastDotIndex == filename.length() - 1) {
            return "";
        }

        return filename.substring(lastDotIndex + 1);
    }
}
