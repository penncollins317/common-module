package top.echovoid.common.exceptions;

/**
 * @author Penn Collins
 * @since 2024/10/17
 */
public class DataNotFoundException extends ServiceException {
    public DataNotFoundException() {
        this("访问的资源不存在");
    }

    public DataNotFoundException(String message) {
        super(message);
    }
}

