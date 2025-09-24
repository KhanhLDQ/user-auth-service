package org.tommap.tomuserloginrestapis.constant;

public class EmailTemplateConstants {
    public static final String EMAIL_VERIFICATION_HTML_TEMPLATE = """
        <html>
        <body>
            <h2>Hi %s!</h2>
            <p>Please verify your email address by clicking the link below:</p>
            <p><a href="%s">Verify Email</a></p>
            <p>This link will expire in %d minutes.</p>
            <p>Best regards,<br>Your Team</p>
        </body>
        </html>
        """;

    public static final String EMAIL_VERIFICATION_TEXT_TEMPLATE = """
        Hi %s!
        
        Please verify your email address by clicking this link:
        %s
        
        This link will expire in %d minutes.
        
        Best regards,
        Your Team
        """;

    public static final String EMAIL_VERIFICATION_SUBJECT = "LAST STEP TO COMPLETE YOUR REGISTRATION WITH TOM-USER-LOGIN APP";
}
