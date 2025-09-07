package org.tommap.tomuserloginrestapis.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.tommap.tomuserloginrestapis.model.dto.UserDto;
import org.tommap.tomuserloginrestapis.model.entity.User;
import org.tommap.tomuserloginrestapis.model.request.UserDetailsRequest;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {UserMapperImpl.class})
public class UserMapperTest {
    @Autowired
    private UserMapper userMapper;

    @Test
    void testRequestToUserDto_ShouldReturnUserDto() {
        //arrange
        var request = new UserDetailsRequest("Tom", "SE", "tom@gmail.com", "123456");

        //act
        var userDto = userMapper.requestToUserDto(request);

        //assert
        assertThat(userDto).isNotNull();
        assertThat(userDto)
                .extracting("firstName", "lastName", "email", "password")
                .containsExactly("Tom", "SE", "tom@gmail.com", "123456");
    }

    @Test
    void testRequestToUserDto_WhenNullProvided_ShouldReturnNull() {
        //arrange & act
        var userDto = userMapper.requestToUserDto(null);

        //assert
        assertThat(userDto).isNull();
    }

    @Test
    void testUserDtoToResponse_ShouldReturnUserRest() {
        //arrange
        var userDto = new UserDto(1L, "user-abc-xyz", "Tom", "SE", "tom@gmail.com", "123456", "encrypted_123456", "email_token", true);

        //act
        var userRest = userMapper.userDtoToResponse(userDto);

        //assert
        assertThat(userRest).isNotNull();
        assertThat(userRest)
                .extracting("userId", "firstName", "lastName", "email")
                .containsExactly("user-abc-xyz", "Tom", "SE", "tom@gmail.com");
    }

    @Test
    void testUserDtoToResponse_WhenNullProvided_ShouldReturnNull() {
        //arrange & act
        var userRest = userMapper.userDtoToResponse(null);

        //assert
        assertThat(userRest).isNull();
    }

    @Test
    void testUserDtoToUserEntity_ShouldReturnUserEntity() {
        //arrange
        var userDto = new UserDto(1L, "user-abc-xyz", "Tom", "SE", "tom@gmail.com", "123456", "encrypted_123456", "email_token", true);

        //act
        var user = userMapper.userDtoToUserEntity(userDto);

        //assert
        assertThat(user).isNotNull();
        assertThat(user)
                .extracting("id", "userId", "firstName", "lastName", "email", "encryptedPassword", "emailVerificationToken", "emailVerificationStatus")
                .containsExactly(1L, "user-abc-xyz", "Tom", "SE", "tom@gmail.com", "encrypted_123456", "email_token", true);
    }

    @Test
    void testUserEntityToUserDto_ShouldReturnUserDto() {
        //arrange
        var user = new User(1L, "user-abc-xyz", "Tom", "SE", "tom@gmail.com", "encrypted_123456", "email_token", true);

        //act
        var userDto = userMapper.userEntityToUserDto(user);

        //assert
        assertThat(userDto).isNotNull();
        assertThat(userDto.getPassword()).isNull();
        assertThat(userDto)
                .extracting("id", "userId", "firstName", "lastName", "email", "encryptedPassword", "emailVerificationToken", "emailVerificationStatus")
                .containsExactly(1L, "user-abc-xyz", "Tom", "SE", "tom@gmail.com", "encrypted_123456", "email_token", true);
    }

    /*
        @ParameterizedTest(name = "[{index}-{1}]")
        @MethodSource("requests")
        void testRequestToUserDto(
                UserDetailsRequest request, String description
        ) {
            //arrange & act
            var userDto = userMapper.requestToUserDto(request);

            //assert
            if (null == request) {
                assertThat(userDto).isNull();
            } else {
                assertThat(userDto).isNotNull();
                assertThat(userDto)
                        .extracting("firstName", "lastName", "email", "password")
                        .containsExactly("Tom", "SE", "tom@gmail.com", "123456");
            }
        }

        private static Stream<Arguments> requests() {
            return Stream.of(
                    Arguments.of(
                            new UserDetailsRequest("Tom", "SE", "tom@gmail.com", "123456"),
                            "valid user request"
                    ),
                    Arguments.of(null, "null user request")
            );
        }
     */
}
