package top.mxzero.filestore.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Peng
 * @since 2025/5/5
 */
@Controller
public class FileUploadPageController {
    @RequestMapping("/upload/page")
    public String uploadPageApi() {
        return "filestore/upload";
    }
}
