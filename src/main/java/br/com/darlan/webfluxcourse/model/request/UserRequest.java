package br.com.darlan.webfluxcourse.model.request;

import br.com.darlan.webfluxcourse.validator.TrimString;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRequest(
        @TrimString
        @NotBlank(message = "must not be null or empty")
        @Size(min = 3, max = 50, message = "must be between 3 and 50 characters")
        String name,
        @NotBlank(message = "must not be null or empty")
        @TrimString
        @Email(message = "invalid e-mail")
        String email,
        @TrimString
        @NotBlank(message = "must not be null or empty")
        @Size(min = 3, max = 50, message = "must be between 3 and 50 characters")
        String password
) {

}
