package com.backoffice.operations.repository;

import com.backoffice.operations.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile, String> {
    Optional<Profile> findByUniqueKeyCivilId(String uniqueKey);

    Optional<Profile> findByUserId(String userId);
}
