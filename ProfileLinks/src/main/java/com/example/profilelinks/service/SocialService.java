package com.example.profilelinks.service;


import com.example.profilelinks.model.dto.SocialDto;
import com.example.profilelinks.model.response.Data;
import com.example.profilelinks.model.response.ListData;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface SocialService {
    ListData getAll(int page, int pageSize);

    Data getById(Long id);

    Data add(SocialDto socialDto);

    Data update(SocialDto socialDto, Long id);

    Data delete(Long id);

    ListData getByProfileId(Pageable pageable, Long profileId);

}
