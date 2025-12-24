package fpt.is.bnk.fptis_platform.repository.auth;

import fpt.is.bnk.fptis_platform.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Admin 12/1/2025
 *
 **/
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("""
                select u from User u
                left join fetch u.profile
                where lower(u.username) = lower(:username)
            """)
    Optional<User> findByUsernameWithProfile(@Param("username") String username);

    @Query("""
                select u from User u
                left join fetch u.profile
                where lower(u.email) = lower(:email)
            """)
    Optional<User> findByEmailWithProfile(@Param("email") String email);

    Optional<User> findUserByEmailIgnoreCase(String email);
}

