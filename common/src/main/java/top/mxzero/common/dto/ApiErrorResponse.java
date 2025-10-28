package top.mxzero.common.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
@Builder
public class ApiErrorResponse {
    private Integer code;
    private String message;
    private List<Map<String, String>> errors;
    private String path;
    private Date timestamp;
}