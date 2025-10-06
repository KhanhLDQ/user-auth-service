package org.tommap.tomuserloginrestapis.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.tommap.tomuserloginrestapis.model.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByUserId(String userId);
    Optional<User> findByEmailVerificationToken(String emailVerificationToken);

    @Query(
        value = "SELECT * FROM users u WHERE u.email_verification_status = true",
        countQuery = "SELECT COUNT(*) FROM users u WHERE u.email_verification_status = true",
        nativeQuery = true
    )
    Page<User> findAllUsersWithVerifiedEmail(Pageable pageable);

    @Query(
        value = "SELECT * FROM users u WHERE u.email_verification_status = ?1 AND u.first_name LIKE %?2%",
        nativeQuery = true
    )
    List<User> findByEmailVerificationStatusAndFirstNamePattern(boolean emailVerificationStatus, String firstName); //positional params

    @Query(
        value = "SELECT * FROM users u WHERE u.email_verification_status = :emailVerificationStatus AND u.last_name LIKE %:lastName%",
        nativeQuery = true
    )
    List<User> findByEmailVerificationStatusAndLastNamePattern(
        @Param("emailVerificationStatus") boolean emailVerificationStatus,
        @Param("lastName") String lastName
    ); //named params

    @Query(
        value = "SELECT u.user_id AS userId, u.first_name AS firstName, u.last_name AS lastName, u.email AS email FROM users u WHERE u.first_name LIKE %:firstName% AND u.last_name LIKE %:lastName%",
        nativeQuery = true
    )
    List<UserBasicInfo> findByFirstNameAndLastNamePattern(
        @Param("firstName") String firstName,
        @Param("lastName") String lastName
    ); //select specific columns

    interface UserBasicInfo {
        String getUserId();
        String getFirstName();
        String getLastName();
        String getEmail();
    }

    @Transactional //should be on service layer
    @Modifying(
        clearAutomatically = true, //clear persistence context AFTER query execution to prevent reading stale cached data
        flushAutomatically = true //flush pending changes to DB BEFORE query execution to prevent data loss
    )
    @Query(
        value = "UPDATE users u SET u.first_name = :firstName, u.last_name = :lastName WHERE u.user_id = :userId",
        nativeQuery = true
    )
    int updateFirstNameAndLastName(
        @Param("userId") String userId, //identifier put first
        @Param("firstName") String firstName,
        @Param("lastName") String lastName
    );

    @Query("SELECT u FROM User u WHERE u.firstName LIKE %:firstName% AND u.lastName LIKE %:lastName%") //JPQL
    List<User> findByFirstNameAndLastNamePatternJPQL(
        @Param("firstName") String firstName,
        @Param("lastName") String lastName
    );

    @Query("SELECT u.userId AS userId, u.firstName AS firstName, u.lastName AS lastName, u.email AS email FROM User u WHERE u.firstName = :firstName") //select specific columns
    List<UserBasicInfo> findByFirstName(@Param("firstName") String firstName);

    @Transactional
    @Modifying(
        clearAutomatically = true,
        flushAutomatically = true
    )
    @Query("UPDATE User u SET u.firstName = :firstName, u.lastName = :lastName WHERE u.userId = :userId")
    int updateFirstNameAndLastNameJPQL(
        @Param("userId") String userId,
        @Param("firstName") String firstName,
        @Param("lastName") String lastName
    );
}
