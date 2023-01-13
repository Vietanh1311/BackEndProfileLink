package com.example.profilelinks.repository;


import com.example.profilelinks.model.entity.SocialEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SocialRepository extends JpaRepository<SocialEntity, Long> {
    Page<SocialEntity> findByProfileId(Pageable pageable, @Param("profile_id") Long profileId);
}
