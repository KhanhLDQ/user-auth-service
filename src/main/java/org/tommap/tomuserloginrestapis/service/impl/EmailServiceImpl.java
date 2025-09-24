package org.tommap.tomuserloginrestapis.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.tommap.tomuserloginrestapis.exception.EmailSendingException;
import org.tommap.tomuserloginrestapis.service.IEmailService;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.Body;
import software.amazon.awssdk.services.ses.model.Content;
import software.amazon.awssdk.services.ses.model.Destination;
import software.amazon.awssdk.services.ses.model.Message;
import software.amazon.awssdk.services.ses.model.SendEmailRequest;
import software.amazon.awssdk.services.ses.model.SendEmailResponse;

import static org.tommap.tomuserloginrestapis.constant.EmailTemplateConstants.EMAIL_VERIFICATION_HTML_TEMPLATE;
import static org.tommap.tomuserloginrestapis.constant.EmailTemplateConstants.EMAIL_VERIFICATION_SUBJECT;
import static org.tommap.tomuserloginrestapis.constant.EmailTemplateConstants.EMAIL_VERIFICATION_TEXT_TEMPLATE;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements IEmailService {
    @Value("${email.sender}")
    private String emailSender;

    @Value("${app.base-url}")
    private String baseUrl;

    private final SesClient sesClient;

    @Override
    public void sendEmailVerification(
        String recipient, String verificationToken, long emailVerificationExpiry, String fullName
    ) {
        String verificationLink = buildVerificationLink(verificationToken);
        String htmlContent = String.format(EMAIL_VERIFICATION_HTML_TEMPLATE,
                fullName, verificationLink, emailVerificationExpiry
        );
        String textContent = String.format(EMAIL_VERIFICATION_TEXT_TEMPLATE,
                fullName, verificationLink, emailVerificationExpiry
        );

        try {
            SendEmailRequest emailRequest = SendEmailRequest.builder()
                    .source(emailSender)
                    .destination(Destination.builder()
                            .toAddresses(recipient)
                            .build())
                    .message(Message.builder()
                            .subject(Content.builder()
                                    .data(EMAIL_VERIFICATION_SUBJECT)
                                    .charset("UTF-8")
                                    .build())
                            .body(Body.builder()
                                    .html(Content.builder()
                                            .data(htmlContent)
                                            .charset("UTF-8")
                                            .build())
                                    .text(Content.builder()
                                            .data(textContent)
                                            .charset("UTF-8")
                                            .build())
                                    .build())
                            .build())
                    .build();

            SendEmailResponse response = sesClient.sendEmail(emailRequest);
            log.info("Email verification sent to user: {} successfully - messageId: {}", recipient, response.messageId());
        } catch (Exception ex) {
            log.error("Failed to send email verification to user: {} - reason: {}", recipient, ex.getMessage());

            throw new EmailSendingException(ex.getMessage());
        }
    }

    private String buildVerificationLink(String verificationToken) {
        return String.format("%s/email-verification?token=%s", baseUrl, verificationToken);
    }
}
