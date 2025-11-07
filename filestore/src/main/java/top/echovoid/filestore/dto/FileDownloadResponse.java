package top.echovoid.filestore.dto;

import lombok.Data;

import java.io.InputStream;

@Data
public class FileDownloadResponse {
    private String filename;
    private InputStream inputStream;
    private long size;
    private String contentType;
}