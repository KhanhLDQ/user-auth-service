package org.tommap.tomuserloginrestapis.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;
import org.tommap.tomuserloginrestapis.event.application_event.ApplicationEvent;
import org.tommap.tomuserloginrestapis.event.publisher.EventPublisher;
import org.tommap.tomuserloginrestapis.exception.EmailVerificationTokenExpiredException;
import org.tommap.tomuserloginrestapis.exception.ResourceAlreadyExistedException;
import org.tommap.tomuserloginrestapis.exception.ResourceNotFoundException;
import org.tommap.tomuserloginrestapis.mapper.UserMapper;
import org.tommap.tomuserloginrestapis.model.dto.UserDto;
import org.tommap.tomuserloginrestapis.model.entity.User;
import org.tommap.tomuserloginrestapis.repository.UserRepository;
import org.tommap.tomuserloginrestapis.service.impl.UserServiceImpl;
import org.tommap.tomuserloginrestapis.shared.EmailUtils;
import org.tommap.tomuserloginrestapis.shared.UserUtils;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private UserUtils userUtils;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private EmailUtils emailUtils;

    @Mock
    private EventPublisher<ApplicationEvent> eventPublisher;

    @Mock
    private ITransactionHandler transactionHandler;

    @InjectMocks
    private UserServiceImpl userService; //require implementation class instead of interface

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(userService, "emailVerificationExpiry", 5); //mock @Value
    }

    //naming convention - test<MethodUnderTest>_<Scenario>_<ExpectedBehavior>
    @Test
    void testGetByUsername_WhenInvalidUsernameProvided_ShouldThrowException() {
        //arrange
        when(userRepository.findByEmail("invalid-email")).thenReturn(Optional.empty());

        //act & assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            userService.getByUsername("invalid-email");
        }, "should throw ResourceNotFoundException in case of invalid username");

        assertThat(exception.getMessage()).isEqualTo("User invalid-email not found");

        verify(userRepository, times(1)).findByEmail("invalid-email");
        verifyNoInteractions(userMapper);
    }

    @Test
    void testCreateUser_WhenUserAlreadyExisted_ShouldThrowException() {
        //arrange
        var user = new User();
        var userDto = new UserDto();
        userDto.setEmail("already-existed-user@gmail.com");

        when(userRepository.findByEmail("already-existed-user@gmail.com")).thenReturn(Optional.of(user));

        //act & assert
        ResourceAlreadyExistedException exception = assertThrows(ResourceAlreadyExistedException.class, () -> {
            userService.createUser(userDto);
        }, "should throw ResourceAlreadyExistedException in case of user already existed");

        assertThat(exception.getMessage()).isEqualTo("User already existed - email: already-existed-user@gmail.com");

        verify(userRepository, times(1)).findByEmail("already-existed-user@gmail.com");
        verify(userRepository, never()).save(any(User.class));
        verifyNoInteractions(passwordEncoder, userUtils, emailUtils, eventPublisher, userMapper);
    }

    @Test
    void testGetByUserId_WhenInvalidUserIdProvided_ShouldThrowException() {
        //arrange
        when(userRepository.findByUserId("invalid-user-id")).thenReturn(Optional.empty());

        //act & assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            userService.getByUserId("invalid-user-id");
        }, "should throw ResourceNotFoundException in case of invalid user id");

        assertThat(exception.getMessage()).isEqualTo("User invalid-user-id not found");

        verify(userRepository, times(1)).findByUserId("invalid-user-id");
        verifyNoInteractions(userMapper);
    }

    @Test
    void testUpdateUser_WhenInvalidUserIdProvided_ShouldThrowException() {
        //arrange
        when(userRepository.findByUserId("invalid-user-id")).thenReturn(Optional.empty());

        //act & assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            userService.updateUser("invalid-user-id", new UserDto());
        }, "should throw ResourceNotFoundException in case of invalid user id");

        assertThat(exception.getMessage()).isEqualTo("User invalid-user-id not found");

        verify(userRepository, times(1)).findByUserId("invalid-user-id");
        verify(userRepository, never()).save(any(User.class));
        verifyNoInteractions(userMapper);
    }

    @Test
    void testDeleteUser_WhenInvalidUserIdProvided_ShouldThrowException() {
        //arrange
        when(userRepository.findByUserId("invalid-user-id")).thenReturn(Optional.empty());

        //act & assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            userService.deleteUser("invalid-user-id");
        }, "should throw ResourceNotFoundException in case of invalid user id");

        assertThat(exception.getMessage()).isEqualTo("User invalid-user-id not found");

        verify(userRepository, times(1)).findByUserId("invalid-user-id");
        verify(userRepository, never()).delete(any(User.class));
    }

    @Test
    void testGetUserAddresses_WhenInvalidUserIdProvided_ShouldThrowException() {
        //arrange
        when(userRepository.findByUserId("invalid-user-id")).thenReturn(Optional.empty());

        //act & assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            userService.getUserAddresses("invalid-user-id");
        }, "should throw ResourceNotFoundException in case of invalid user id");

        assertThat(exception.getMessage()).isEqualTo("User invalid-user-id not found");

        verify(userRepository, times(1)).findByUserId("invalid-user-id");
        verifyNoInteractions(userMapper);
    }

    @Test
    void testVerifyEmailToken_WhenInvalidTokenProvided_ShouldReturnFalse() {
        //arrange
        when(userRepository.findByEmailVerificationToken("invalid-token")).thenReturn(Optional.empty());

        //act & assert
        assertThat(userService.verifyEmailToken("invalid-token")).isFalse();
    }

    @Test
    void testVerifyEmailToken_WhenExpiredTokenProvided_ShouldReturnFalse() {
        //arrange
        var user = new User();
        user.setEmailTokenExpiry(LocalDateTime.now().minusHours(1));

        when(userRepository.findByEmailVerificationToken("expired-token")).thenReturn(Optional.of(user));

        //act
        boolean isVerified = userService.verifyEmailToken("expired-token");

        //assert
        assertThat(isVerified).isFalse();

        verify(userRepository, times(1))
                .save(argThat(u -> null == u.getEmailVerificationToken() && null == u.getEmailTokenExpiry())); //capture param
    }

    @Test
    void testVerifyEmailToken_WhenValidTokenProvided_ShouldReturnTrue() {
        //arrange
        var user = new User();
        user.setEmailTokenExpiry(LocalDateTime.now().plusHours(1));

        when(userRepository.findByEmailVerificationToken("valid-token")).thenReturn(Optional.of(user));

        //act
        boolean isVerified = userService.verifyEmailToken("valid-token");

        //act
        assertThat(isVerified).isTrue();

        verify(userRepository, times(1))
                .save(argThat(u -> null == u.getEmailVerificationToken()
                        && null == u.getEmailTokenExpiry()
                        && u.isEmailVerificationStatus()
                ));
    }

    @Test
    void testResendEmailVerification_WhenInvalidEmailProvided_ShouldThrowException() {
        //arrange
        when(userRepository.findByEmail("invalid-email")).thenReturn(Optional.empty());

        //act & assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            userService.resendEmailVerification("invalid-email");
        }, "should throw ResourceNotFoundException in case of invalid email");

        assertThat(exception.getMessage()).isEqualTo("User invalid-email not found");

        verify(userRepository, never()).save(any(User.class));
        verifyNoInteractions(eventPublisher);
    }

    @Test
    void testResendEmailVerification_WhenEmailAlreadyVerified_ShouldDoNothing() {
        //arrange
        var user = new User();
        user.setEmailVerificationStatus(true);

        when(userRepository.findByEmail("email-already-verified")).thenReturn(Optional.of(user));

        //act
        userService.resendEmailVerification("email-already-verified");

        //assert
        verify(userRepository, never()).save(any(User.class));
        verifyNoInteractions(eventPublisher);
    }

    @Test
    void testResetPasswordRequest_WhenInvalidEmailProvided_ShouldThrowException() {
        //arrange
        when(userRepository.findByEmail("invalid-email")).thenReturn(Optional.empty());

        //act & assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            userService.resetPasswordRequest("invalid-email");
        }, "should throw ResourceNotFoundException in case of invalid email");

        assertThat(exception.getMessage()).isEqualTo("User invalid-email not found");

        verify(userRepository, never()).save(any(User.class));
        verifyNoInteractions(eventPublisher);
    }

    @Test
    void testResetPassword_WhenInvalidEmailTokenProvided_ShouldThrowException() {
        //arrange
        when(userRepository.findByEmailVerificationToken("invalid-token")).thenReturn(Optional.empty());

        //act & assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            userService.resetPassword("invalid-token", "new-password");
        }, "should throw ResourceNotFoundException in case of invalid token");

        assertThat(exception.getMessage()).isEqualTo("User with token invalid-token not found");

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testResetPassword_WhenExpiredTokenProvided_ShouldThrowException() {
        //arrange
        var user = new User();
        user.setEmailTokenExpiry(LocalDateTime.now().minusHours(1));

        when(userRepository.findByEmailVerificationToken("expired-token")).thenReturn(Optional.of(user));
        when(transactionHandler.runInNewTransaction(any())).thenAnswer(invocation -> { //invocation: current method call
            Supplier<?> supplier = invocation.getArgument(0);
            return supplier.get();
        });

        //act & assert
        EmailVerificationTokenExpiredException exception = assertThrows(EmailVerificationTokenExpiredException.class, () -> {
            userService.resetPassword("expired-token", "new-password");
        }, "should throw EmailVerificationTokenExpiredException in case of expired token");

        assertThat(exception.getMessage()).isEqualTo("Email verification token already expired!");

        verify(transactionHandler, times(1)).runInNewTransaction(any());
        verify(userRepository, times(1))
                .save(argThat(u -> null == u.getEmailVerificationToken() && null == u.getEmailTokenExpiry()));
    }
}
