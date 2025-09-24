package org.tommap.tomuserloginrestapis.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.tommap.tomuserloginrestapis.exception.AwsSesInitException;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ses.SesClient;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class AwsSesConfig {
    private final AwsProperties awsProperties;

    @Bean
    public SesClient sesClient() {
        log.info("Init SesClient using properties: {}", awsProperties); //only debugging - should not log credentials to console

        try {
            AwsCredentialsProvider awsCredentialsProvider = StaticCredentialsProvider.create(
                AwsBasicCredentials.create(
                    awsProperties.getAccessKeyId(), awsProperties.getSecretKey()
                )
            );

            return SesClient.builder()
                    .region(Region.of(awsProperties.getRegion()))
                    .credentialsProvider(awsCredentialsProvider)
                    .build();
        } catch (Exception ex) {
            log.error("Failed to init aws ses client {}", ex.getMessage());

            throw new AwsSesInitException(ex.getMessage());
        }
    }
}
