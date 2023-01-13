package com.example.profilelinks.service;


import com.example.profilelinks.model.dto.LinkDto;
import com.example.profilelinks.model.response.Data;
import com.example.profilelinks.model.response.ListData;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface LinkService {
    Data getById(Long id);

    ListData getByProfileId(Pageable pageable, Long profileId);

    Data add(LinkDto linkDTO, MultipartFile file);

    Data update(LinkDto linkDTO, MultipartFile file, Long id);

    Data delete(Long id);

}
