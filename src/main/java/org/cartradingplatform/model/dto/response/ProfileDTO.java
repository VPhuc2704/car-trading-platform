package org.cartradingplatform.model.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.cartradingplatform.model.enums.Gender;
import java.time.LocalDate;

@Data
public class ProfileDTO {
    private Long id;
    private String email;
    private String passwordHash;
    private String fullName;
    private String numberPhone;
    private LocalDate dateOfBirth;
    private Gender gender;
}
