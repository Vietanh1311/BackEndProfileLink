package com.example.profilelinks.service;


import com.example.profilelinks.model.dto.StatisticDto;
import com.example.profilelinks.model.response.Data;
import com.example.profilelinks.model.response.ListData;

public interface StatisticService {

    ListData getAll(int page, int pageSize);

    Data getById(Long id);

    Data add(StatisticDto statisticDto);

    Data update(StatisticDto statisticDto, Long id);

    Data delete(Long id);

    // ListData getByProfileId(Pageable pageable,Integer profileId);
    ListData getTopProfileToMonth(int page, int pageSize, int month, int year);
}
