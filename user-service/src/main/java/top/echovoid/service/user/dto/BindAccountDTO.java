package top.echovoid.service.user.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import top.echovoid.service.user.enums.AccountType;

/**
 * @author Peng
 * @since 2025/4/13
 */
@Data
public class BindAccountDTO {
    @NotNull
    private AccountType accountType;
    @NotBlank
    @Length(max = 100)
    private String accountValue;

    @JsonIgnore
    private Long userId;
}
