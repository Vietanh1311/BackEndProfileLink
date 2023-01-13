package com.example.profilelinks.repository;

import com.example.profilelinks.constants.StatusEnum;
import com.example.profilelinks.model.entity.ProfileEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<ProfileEntity, Long> {

    Optional<ProfileEntity> getProfileByShortBioAndStatus(String shortBio, StatusEnum status);

    ProfileEntity getProfileByShortBio(String shortBio);

    @Query("SELECT p FROM ProfileEntity p where p.status = 1 ORDER BY p.clickCount DESC")
    Page<ProfileEntity> getTopProfile(Pageable pageable);

}
