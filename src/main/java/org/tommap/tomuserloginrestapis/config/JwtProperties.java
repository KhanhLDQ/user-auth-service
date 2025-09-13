package org.tommap.tomuserloginrestapis.config;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@ConfigurationProperties(prefix = "jwt")
@Getter @Setter
@NoArgsConstructor
@Validated
public class JwtProperties {
    @NotBlank(message = "jwt secret cannot be blank!")
    private String secret;

    @Positive(message = "jwt expiration should be positive!")
    private long expiration;
}
