package fpt.is.bnk.fptis_platform.repository;

import fpt.is.bnk.fptis_platform.entity.user.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Admin 11/25/2025
 *
 **/
@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {

//    Optional<Profile> findByUserId(String userId);

}
