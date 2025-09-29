package org.tommap.tomuserloginrestapis.controller;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.tommap.tomuserloginrestapis.AbstractIntegrationTest;
import org.tommap.tomuserloginrestapis.exception.ResourceNotFoundException;
import org.tommap.tomuserloginrestapis.model.request.CreateUserRequest;
import org.tommap.tomuserloginrestapis.model.request.ResetPassword;
import org.tommap.tomuserloginrestapis.model.request.ResetPasswordRequest;
import org.tommap.tomuserloginrestapis.model.request.UpdateUserRequest;
import org.tommap.tomuserloginrestapis.model.request.UserLoginRequest;
import org.tommap.tomuserloginrestapis.repository.UserRepository;
import software.amazon.awssdk.services.ses.model.SendEmailRequest;
import software.amazon.awssdk.services.ses.model.SendEmailResponse;

import java.util.List;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/*
    - use LoggingFilter to apply on entire test class instead of log() on each test method
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserControllerTest extends AbstractIntegrationTest {
    @Value("${app.base-url}")
    private String baseUrl;

    @LocalServerPort
    private int port;

    @Autowired
    private UserRepository userRepository;

    private String emailVerificationToken;
    private String jwt;
    private String userId;

    @BeforeAll
    void setUpBeforeAll() {
        RestAssured.baseURI = baseUrl;
        RestAssured.port = port;
        RestAssured.basePath = "/api/v1";
    }

    @BeforeEach
    void setUpBeforeEach() {
        var mockEmailResponse = SendEmailResponse.builder()
                .messageId("message-id-1")
                .build();
        when(sesClient.sendEmail(any(SendEmailRequest.class))).thenReturn(mockEmailResponse);
    }

    @Test
    @Order(1)
    void testContainerIsRunning() {
        assertTrue(mysql.isCreated(), "MySQL container is not created");
        assertTrue(mysql.isRunning(), "MySQL container is not running");
    }

    @Test
    @Order(2)
    void testCreateUser_WhenValidRequestProvided_ShouldReturnCreatedUser() {
        //arrange
        var addressRequest = new CreateUserRequest.CreateAddressRequest("Da Nang", "Vietnam", "123 Hai Phong str", "550000", "BILLING");
        var createUserRequest = new CreateUserRequest("Tom", "Engineer", "tomtest@gmail.com", "Khanh12345@K", List.of(addressRequest));

        //act & assert
        given() //setup HTTP request details -> headers - params - body
            .log()
                .all() //help to debug a failed test method
            .contentType(JSON)
            .accept(JSON)
            .body(createUserRequest)
        .when() //specify HTTP method & endpoint to call
            .post("/users")
        .then() //verify HTTP response
            .log()
                .all()
            .statusCode(201)
            .body("statusCode", equalTo(201))
            .body("message", equalTo("User created successfully"))
            .body("data.userId", notNullValue())
            .body("data.firstName", equalTo("Tom"))
            .body("data.lastName", equalTo("Engineer"))
            .body("data.email", equalTo("tomtest@gmail.com"))
            .body("data.addresses", hasSize(1))
            .body("data.addresses[0].id", notNullValue())
            .body("data.addresses[0].city", equalTo("Da Nang"))
            .body("data.addresses[0].country", equalTo("Vietnam"))
            .body("data.addresses[0].street", equalTo("123 Hai Phong str"))
            .body("data.addresses[0].postalCode", equalTo("550000"))
            .body("data.addresses[0].type", equalTo("BILLING"));
    }

    @Test
    @Order(3)
    void testLogin_WhenUserEmailNotVerified_ShouldReturnUnauthorized() {
        //arrange
        var loginRequest = new UserLoginRequest("tomtest@gmail.com", "Khanh12345@K");

        //act & assert
        given()
            .log()
                .all()
            .contentType(JSON)
            .accept(JSON)
            .body(loginRequest)
        .when()
            .post("/login")
        .then()
            .log()
                .all()
            .statusCode(401)
            .body("statusCode", equalTo(401))
            .body("message", equalTo("Email verification required. Please verify your email to complete authentication!"))
            .body("data", nullValue());
    }

    @Test
    @Order(4)
    void testVerifyEmailToken_WhenValidTokenProvided_ShouldVerifyEmailSuccessfully() {
        //arrange
        var user = userRepository.findByEmail("tomtest@gmail.com")
                .orElseThrow(() -> new ResourceNotFoundException("User tomtest@gmail.com not found"));
        emailVerificationToken = user.getEmailVerificationToken();

        //act & assert
        given()
            .log()
                .all()
            .accept(JSON)
            .queryParam("token", emailVerificationToken)
        .when()
            .get("/users/email-verification")
        .then()
            .log()
                .all()
            .statusCode(200)
            .body("statusCode", equalTo(200))
            .body("message", equalTo("Email verified successfully"))
            .body("data", nullValue());
    }

    @Test
    @Order(5)
    void testLogin_WhenEmailVerifiedAndValidCredentialsProvided_ShouldReturnLoginResponse() {
        //arrange
        var loginRequest = new UserLoginRequest("tomtest@gmail.com", "Khanh12345@K");

        //act & assert
        Response response =
                given()
                    .log()
                        .all()
                    .contentType(JSON)
                    .accept(JSON)
                    .body(loginRequest)
                .when()
                    .post("/login")
                .then()
                    .log()
                        .all()
                    .statusCode(200)
                    .body("statusCode", equalTo(200))
                    .body("message", equalTo("Successful authentication"))
                    .body("data.jwt", notNullValue())
                    .body("data.userId", notNullValue())
                    .body("data.username", equalTo("tomtest@gmail.com"))
                    .extract()
                        .response();

        jwt = response.path("data.jwt");
        userId = response.path("data.userId");
    }

    @Test
    @Order(6)
    void testGetUserDetails_WhenMissingJwt_ShouldReturnForbidden() {
        //arrange

        //act & assert
        given()
            .log()
                .all()
            .accept(JSON)
            .pathParam("userId", userId)
        .when()
            .get("/users/{userId}")
        .then()
            .log()
                .all()
            .statusCode(401);
    }

    @Test
    @Order(7)
    void testGetUserDetails_WhenValidUserIdProvided_ShouldReturnUserDetails() {
        //arrange

        //act & assert
        given()
            .log()
                .all()
            .accept(JSON)
            .pathParam("userId", userId)
            .auth()
                .oauth2(jwt) // bearer token -> not show log due to sensitive data
        .when()
            .get("/users/{userId}")
        .then()
            .log()
                .all()
            .statusCode(200)
            .body("statusCode", equalTo(200))
            .body("message", equalTo("Get user details successfully"))
            .body("data.userId", equalTo(userId))
            .body("data.firstName", equalTo("Tom"))
            .body("data.lastName", equalTo("Engineer"))
            .body("data.email", equalTo("tomtest@gmail.com"))
            .body("data.addresses[0].id", notNullValue())
            .body("data.addresses[0].city", equalTo("Da Nang"))
            .body("data.addresses[0].country", equalTo("Vietnam"))
            .body("data.addresses[0].street", equalTo("123 Hai Phong str"))
            .body("data.addresses[0].postalCode", equalTo("550000"))
            .body("data.addresses[0].type", equalTo("BILLING"));
    }

    @Test
    @Order(8)
    void testGetUsers_WhenValidParamsProvided_ShouldReturnUsers() {
        //arrange

        //act & assert
        given()
            .log()
                .all()
            .queryParam("page", 0)
            .queryParam("size", 5)
            .auth()
                .oauth2(jwt)
        .when()
            .get("/users")
        .then()
            .log()
                .all()
            .statusCode(200)
            .body("statusCode", equalTo(200))
            .body("message", equalTo("Get users successfully"))
            .body("data.content", hasSize(1))
            .body("data.content[0].userId", equalTo(userId))
            .body("data.content[0].email", equalTo("tomtest@gmail.com"))
            .body("data.pageInfo.currentPage", equalTo(0))
            .body("data.pageInfo.numOfElements", equalTo(1))
            .body("data.pageInfo.pageSize", equalTo(5))
            .body("data.pageInfo.totalPages", equalTo(1))
            .body("data.pageInfo.totalElements", equalTo(1))
            .body("data.pageInfo.hasNext", equalTo(false))
            .body("data.pageInfo.hasPrevious", equalTo(false));
    }

    @Test
    @Order(9)
    void testUpdateUser_WhenValidRequestProvided_ShouldReturnUpdatedUser() {
        //arrange
        var updateUserRequest = new UpdateUserRequest("Khanh", "Le");

        //act & assert
        given()
            .log()
                .all()
            .contentType(JSON)
            .accept(JSON)
            .pathParam("userId", userId)
            .body(updateUserRequest)
            .auth()
                .oauth2(jwt)
        .when()
            .put("/users/{userId}")
        .then()
            .log()
                .all()
            .statusCode(200)
            .body("statusCode", equalTo(200))
            .body("message", equalTo("User updated successfully"))
            .body("data.userId", equalTo(userId))
            .body("data.firstName", equalTo("Khanh"))
            .body("data.lastName", equalTo("Le"));
    }

    @Test
    @Order(10)
    void testResetPasswordRequest_WhenValidEmailProvided_ShouldSendRequest() {
        //arrange
        var resetPasswordRequest = new ResetPasswordRequest("tomtest@gmail.com");

        //act & assert
        given()
            .log()
                .all()
            .contentType(JSON)
            .accept(JSON)
            .body(resetPasswordRequest)
        .when()
            .post("/users/reset-password-request")
        .then()
            .log()
                .all()
            .statusCode(200)
            .body("statusCode", equalTo(200))
            .body("message", equalTo("Reset password request successfully"));
    }

    @Test
    @Order(11)
    void testResetPassword_WhenValidRequestProvided_ShouldResetPasswordSuccessfully() {
        //arrange
        var user = userRepository.findByEmail("tomtest@gmail.com")
                .orElseThrow(() -> new ResourceNotFoundException("User tomtest@gmail.com not found"));
        emailVerificationToken = user.getEmailVerificationToken();

        var resetPassword = new ResetPassword(emailVerificationToken, "Khanhle02091997@K", "Khanhle02091997@K");

        //act & assert
        given()
            .log()
                .all()
            .contentType(JSON)
            .accept(JSON)
            .body(resetPassword)
        .when()
            .post("/users/reset-password")
        .then()
            .log()
                .all()
            .statusCode(200)
            .body("statusCode", equalTo(200))
            .body("message", equalTo("Reset password successfully"));
    }

    @Test
    @Order(12)
    void testDeleteUser_WhenValidUserIdProvided_ShouldDeleteSuccessfully() {
        //arrange

        //act & assert
        given()
            .log()
                .all()
            .accept(JSON)
            .pathParam("userId", userId)
            .auth()
                .oauth2(jwt)
        .when()
            .delete("/users/{userId}")
        .then()
            .log()
                .all()
            .statusCode(200)
            .body("statusCode", equalTo(200))
            .body("message", equalTo("User deleted successfully"));
    }
}
