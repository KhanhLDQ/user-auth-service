package org.tommap.tomuserloginrestapis.service;

public interface IEmailService {
    void sendEmailVerification(String recipient, String verificationToken, long emailVerificationExpiry, String fullName);
}
