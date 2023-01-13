package com.example.profilelinks.service.impl;


import com.example.profilelinks.model.dto.StatisticDto;
import com.example.profilelinks.model.entity.StatisticEntity;
import com.example.profilelinks.model.response.Data;
import com.example.profilelinks.model.response.ListData;
import com.example.profilelinks.model.response.Pagination;
import com.example.profilelinks.repository.StatisticRepository;
import com.example.profilelinks.service.StatisticService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
public class StatisticServiceImpl implements StatisticService {

    private final StatisticRepository statisticRepository;

    private final ModelMapper modelMapper;

    public StatisticServiceImpl(StatisticRepository statisticRepository, ModelMapper modelMapper) {
        this.statisticRepository = statisticRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public Data getById(Long id) {
        // TODO Auto-generated method stub
        StatisticEntity statistic = statisticRepository.findById(id).orElseThrow(() -> new EntityNotFoundException());
        return new Data(true, "success", modelMapper.map(statistic, StatisticDto.class));
    }

    @Override
    public Data add(StatisticDto statisticDto) {
        // TODO Auto-generated method stub
        StatisticEntity statistic = modelMapper.map(statisticDto, StatisticEntity.class);
        return new Data(true, "success", modelMapper.map(statisticRepository.save(statistic), StatisticDto.class));
    }

    @Override
    public Data update(StatisticDto statisticDto, Long id) {
        // TODO Auto-generated method stub
        if (!statisticRepository.existsById(statisticDto.getId()))
            throw new EntityNotFoundException();
        StatisticEntity statisticRepo = statisticRepository.getById(statisticDto.getId());
        StatisticEntity statistic = modelMapper.map(statisticDto, StatisticEntity.class);
        statistic.setId(statisticRepo.getId());
        return new Data(true, "success", modelMapper.map(statisticRepository.save(statistic), StatisticDto.class));
    }

    @Override
    public Data delete(Long id) {
        StatisticEntity statistic = statisticRepository.findById(id).orElseThrow(() -> new EntityNotFoundException());
        statisticRepository.deleteById(id);
        return new Data(true, "success", modelMapper.map(statistic, StatisticDto.class));
    }

    @Override
    public ListData getAll(int page, int pageSize) {
        // TODO Auto-generated method stub
        Page<StatisticEntity> statisticEntities = statisticRepository.findAll(PageRequest.of(page, pageSize));
        return new ListData(true, "success", statisticEntities.getContent(),
                new Pagination(statisticEntities.getNumber(), statisticEntities.getSize(), statisticEntities.getTotalPages(),
                        (int) statisticEntities.getTotalElements()));
    }

    @Override
    public ListData getTopProfileToMonth(int page, int pageSize, int month, int year) {
        Page<StatisticDto> statisticEntities = statisticRepository.getTopProfileToMonth(PageRequest.of(page, pageSize),month,year);
        return new ListData(true, "success", statisticEntities.getContent(),
                new Pagination(statisticEntities.getNumber(), statisticEntities.getSize(), statisticEntities.getTotalPages(),
                        (int) statisticEntities.getTotalElements()));
    }

    // @Override
    // public ListData getByProfileId(Pageable pageable,Integer profileId) {
    // // TODO Auto-generated method stub
    // Page<statisticEntity> pageDesign = statisticRepository.findByProfileId(pageable,
    // profileId);
    // Pagination pagination = new Pagination(pageDesign.getNumber(),
    // pageDesign.getSize(), pageDesign.getTotalPages(), (int)
    // pageDesign.getTotalElements());
    // List<statisticDto> listStatisticDto = pageDesign.stream().map(d ->
    // modelMapper.map(d, statisticDto.class)).collect(Collectors.toList());
    // return new ListData(true, "success", listStatisticDto, pagination);
    // }

}
