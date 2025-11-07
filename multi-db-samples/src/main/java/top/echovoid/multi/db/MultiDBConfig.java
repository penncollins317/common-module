package top.echovoid.multi.db;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Peng
 * @since 2025/4/25
 */
@SpringBootApplication
@MapperScan("top.echovoid.multi.db.mapper")
public class MultiDBConfig {
}
