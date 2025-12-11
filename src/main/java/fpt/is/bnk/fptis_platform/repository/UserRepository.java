package fpt.is.bnk.fptis_platform.repository;

import fpt.is.bnk.fptis_platform.entity.Profile;
import fpt.is.bnk.fptis_platform.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Admin 12/1/2025
 *
 **/
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

