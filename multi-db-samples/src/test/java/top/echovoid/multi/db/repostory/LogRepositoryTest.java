package top.echovoid.multi.db.repostory;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import top.echovoid.multi.db.MultiDBConfig;
import top.echovoid.multi.db.entity.LogTable;
import top.echovoid.multi.db.mapper.LogMapper;

import java.util.Date;

/**
 * @author Penn Collins
 * @since 2025/4/25
 */
@SpringBootTest(classes = MultiDBConfig.class)
public class LogRepositoryTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogRepositoryTest.class);
    @Autowired
    private LogMapper logMapper;

    @Test
    public void testInsert() {
        for (int i = 0; i < 100; i++) {
            LogTable logTable = new LogTable();
            logTable.setText("log - " + i);
            logTable.setCreatedAt(new Date());
            this.logMapper.insert(logTable);
        }
    }

    @Test
    public void testList() {
        this.logMapper.selectList(null).forEach(item -> {
            LOGGER.info(item.toString());
        });
    }
}