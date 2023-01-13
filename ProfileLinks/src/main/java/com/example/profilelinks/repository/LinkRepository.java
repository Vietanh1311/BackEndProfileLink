package com.example.profilelinks.repository;

import com.example.profilelinks.model.entity.LinkEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LinkRepository extends JpaRepository<LinkEntity, Long> {
    Page<LinkEntity> findByProfileId(Pageable pageable, @Param("profile_id") Long profileId);
}
