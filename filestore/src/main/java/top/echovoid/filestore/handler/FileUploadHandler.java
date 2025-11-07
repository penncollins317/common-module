package top.echovoid.filestore.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Part;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.function.HandlerFunction;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;
import top.echovoid.common.dto.RestData;
import top.echovoid.common.utils.FileUtils;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * @author Peng
 * @since 2025/4/28
 */
@Slf4j
public class FileUploadHandler implements HandlerFunction<ServerResponse> {
    @Override
    public ServerResponse handle(ServerRequest request) throws Exception {
        try {
            Optional<String> param = request.param("file");
            log.info("param；{}", param.orElse("空"));
            List<Part> files = request.multipartData().get("file");
            for (Part file : files) {
                if (file.getContentType() == null) {
                    continue;
                }
                log.info("filename: {}, type {}, extend name: {}", file.getSubmittedFileName(), file.getContentType(), FileUtils.getExtendName(file.getSubmittedFileName()));
            }
            RestData<?> data = RestData.success();
            return ServerResponse.status(HttpStatus.CREATED).body(data);
        } catch (IOException | ServletException e) {
            throw new RuntimeException(e);
        }

    }
}
