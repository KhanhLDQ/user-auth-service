package org.tommap.tomuserloginrestapis.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.tommap.tomuserloginrestapis.model.entity.User;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

/*
    - create minimal application context with only JPA-related components
    - transactions roll back after each test [default] to ensure test isolation
    - H2 in-memory db used [default]
 */
@DataJpaTest
@ActiveProfiles("test")
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        var userI = new User();
        userI.setUserId("user-1");
        userI.setFirstName("Tom-1");
        userI.setLastName("Engineer-1");
        userI.setEmail("tom-1@gmail.com");
        userI.setEncryptedPassword("hash-pw-1");
        userI.setEmailVerificationToken("email-tom-1-token");

        var userII = new User();
        userII.setUserId("user-2");
        userII.setFirstName("Tom-2");
        userII.setLastName("Engineer-2");
        userII.setEmail("tom-2@gmail.com");
        userII.setEncryptedPassword("hash-pw-2");
        userII.setEmailVerificationStatus(true);

        userRepository.saveAll(List.of(userI, userII));
    }

    @Test
    void testFindByEmail_WhenValidEmailProvided_ShouldReturnUserEntity() {
        //arrange
        String email = "tom-1@gmail.com";

        //act
        Optional<User> userOptional = userRepository.findByEmail(email);

        //assert
        assertThat(userOptional).isPresent();
        assertThat(userOptional.get())
                .extracting("userId", "firstName", "lastName", "email")
                .containsExactly("user-1", "Tom-1", "Engineer-1", "tom-1@gmail.com");
    }

    @Test
    void testFindByUserId_WhenValidUserIdProvided_ShouldReturnUserEntity() {
        //arrange
        String userId = "user-1";

        //act
        Optional<User> userOptional = userRepository.findByUserId(userId);

        //assert
        assertThat(userOptional).isPresent();
        assertThat(userOptional.get())
                .extracting("userId", "firstName", "lastName", "email")
                .containsExactly("user-1", "Tom-1", "Engineer-1", "tom-1@gmail.com");
    }

    @Test
    void testFindByEmailVerificationToken_WhenValidTokenProvided_ShouldReturnUserEntity() {
        //arrange
        String token = "email-tom-1-token";

        //act
        Optional<User> userOptional = userRepository.findByEmailVerificationToken(token);

        //assert
        assertThat(userOptional).isPresent();
        assertThat(userOptional.get())
                .extracting("userId", "firstName", "lastName", "email")
                .containsExactly("user-1", "Tom-1", "Engineer-1", "tom-1@gmail.com");
    }

    @Test
    void testFindAllUsersWithVerifiedEmail_ShouldReturnUsers() {
        //arrange
        Pageable pageable = PageRequest.of(0,2);

        //act
        Page<User> users = userRepository.findAllUsersWithVerifiedEmail(pageable);

        //assert
        assertThat(users.getContent()).hasSize(1);
        assertThat(users.getContent().get(0))
                .extracting("userId", "firstName", "lastName", "email")
                .containsExactly("user-2", "Tom-2", "Engineer-2", "tom-2@gmail.com");

        assertThat(users.getNumber()).isZero();
        assertThat(users.getTotalElements()).isEqualTo(1);
        assertThat(users.getTotalPages()).isEqualTo(1);
        assertThat(users.hasPrevious() || users.hasNext()).isFalse();
    }

    @Test
    void testFindByEmailVerificationStatusAndFirstNamePattern() {
        //arrange
        boolean emailVerificationStatus = false;
        String firstName = "Tom";

        //act
        List<User> users = userRepository.findByEmailVerificationStatusAndFirstNamePattern(emailVerificationStatus, firstName);

        //assert
        assertThat(users).hasSize(1);
        assertThat(users.get(0))
                .extracting("userId", "firstName", "lastName", "email")
                .containsExactly("user-1", "Tom-1", "Engineer-1", "tom-1@gmail.com");
    }

    @Test
    void testFindByEmailVerificationStatusAndLastNamePattern() {
        //arrange
        boolean emailVerificationStatus = true;
        String lastName = "Eng";

        //act
        List<User> users = userRepository.findByEmailVerificationStatusAndLastNamePattern(emailVerificationStatus, lastName);

        //assert
        assertThat(users).hasSize(1);
        assertThat(users.get(0))
                .extracting("userId", "firstName", "lastName", "email")
                .containsExactly("user-2", "Tom-2", "Engineer-2", "tom-2@gmail.com");
    }

    @Test
    void testFindByFirstNameAndLastNamePattern() {
        //arrange
        String firstName = "Tom";
        String lastName = "Eng";

        //act
        List<UserRepository.UserBasicInfo> users = userRepository.findByFirstNameAndLastNamePattern(firstName, lastName);

        //assert
        assertThat(users).hasSize(2);
        assertThat(users)
                .extracting(
                        UserRepository.UserBasicInfo::getUserId,
                        UserRepository.UserBasicInfo::getFirstName,
                        UserRepository.UserBasicInfo::getLastName,
                        UserRepository.UserBasicInfo::getEmail
                )
                .containsExactlyInAnyOrder(
                        tuple("user-1", "Tom-1", "Engineer-1", "tom-1@gmail.com"),
                        tuple("user-2", "Tom-2", "Engineer-2", "tom-2@gmail.com")
                );
    }

    @Test
    void testUpdateFirstNameAndLastName() {
        //arrange
        String firstName = "Khanh";
        String lastName = "Le";
        String userId = "user-1";

        //act
        int affectedRows = userRepository.updateFirstNameAndLastName(userId, firstName, lastName);

        //assert
        assertThat(affectedRows).isEqualTo(1);

        Optional<User> userOptional = userRepository.findByUserId(userId);
        assertThat(userOptional).isPresent();
        assertThat(userOptional.get())
                .extracting("firstName", "lastName")
                .containsExactly(firstName, lastName);
    }
}
