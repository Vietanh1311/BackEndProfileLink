package com.example.profilelinks.service;

import com.example.profilelinks.constants.DesignTypeEnum;
import com.example.profilelinks.constants.StatusEnum;
import com.example.profilelinks.model.dto.DesignDto;
import com.example.profilelinks.model.response.Data;
import com.example.profilelinks.model.response.ListData;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface DesignService {
    Data getById(Long id);

    ListData getListDesignByType(Pageable pageable, DesignTypeEnum typeDesign, StatusEnum status, String name);

    Data add(DesignDto designDto, MultipartFile avatar, MultipartFile backgroundImage);

    Data update(DesignDto designDto, MultipartFile avatar, MultipartFile backgroundImage, Long id);

    Data delete(Long id);

    Data deleteListDesign(List<DesignDto> listDesign);

    Data findByName(String name);
}
