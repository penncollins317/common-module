package top.echovoid.service.user.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import top.echovoid.common.utils.PhoneUtil;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author Penn Collins
 * @since 2025/5/5
 */
@Data
public class UserAddressDTO {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    private String name;
    private String phone;
    private String province;
    private String city;
    private String district;
    private String detail;
    private String street;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private Boolean isDefault;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public String getPhone() {
        return PhoneUtil.maskPhoneNumber(this.phone);
    }
}

