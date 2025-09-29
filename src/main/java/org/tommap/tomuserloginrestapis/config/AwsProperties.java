package org.tommap.tomuserloginrestapis.config;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@ConfigurationProperties(prefix = "aws")
@Getter @Setter
@NoArgsConstructor
@Validated
@ToString
@Profile("!test")
public class AwsProperties {
    @NotBlank(message = "access-key-id should be required & not blank!")
    private String accessKeyId;

    @NotBlank(message = "secret-key should be required & not blank!")
    private String secretKey;

    @Pattern(regexp = "^[a-z]{2}-[a-z]+-[0-9]$", message = "")
    private String region;
}
