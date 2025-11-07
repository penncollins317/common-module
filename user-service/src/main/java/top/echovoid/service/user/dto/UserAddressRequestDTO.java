package top.echovoid.service.user.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;

/**
 * @author Peng
 * @since 2025/5/5
 */
@Data
public class UserAddressRequestDTO {
    @NotBlank
    @Length(max = 10)
    private String name;
    @NotBlank
    @Length(max = 11, min = 11)
    @Pattern(regexp = "1[3-9]\\d{9}")
    private String phone;
    @NotBlank
    private String province;
    @NotBlank
    private String city;
    @NotBlank
    private String district;
    @NotBlank
    private String detail;
    private String street;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private Boolean isDefault;

    @JsonIgnore
    private Long userId;
}
