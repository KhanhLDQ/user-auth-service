package org.tommap.tomuserloginrestapis.shared;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {EmailUtils.class})
public class EmailUtilsTest {
    @Autowired
    private EmailUtils emailUtils;

    @Test
    void testGenerateEmailVerificationToken_ShouldReturnUUID() {
        //arrange
        //act
        var token = emailUtils.generateEmailVerificationToken();

        //assert
        assertThat(token.length()).isEqualTo(36);
    }

    @Test
    void testGenerateEmailVerificationToken_ShouldBeUnique() {
        //arrange
        //act
        var tokenI = emailUtils.generateEmailVerificationToken();
        var tokenII = emailUtils.generateEmailVerificationToken();

        //assert
        assertThat(tokenI).isNotEqualTo(tokenII);
    }
}
