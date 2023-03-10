package com.example.profilelinks.service.impl;



import com.example.profilelinks.model.dto.SocialDto;
import com.example.profilelinks.model.entity.SocialEntity;
import com.example.profilelinks.model.response.Data;
import com.example.profilelinks.model.response.ListData;
import com.example.profilelinks.model.response.Pagination;
import com.example.profilelinks.repository.ProfileRepository;
import com.example.profilelinks.repository.SocialRepository;
import com.example.profilelinks.service.SocialService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SocialServiceImpl implements SocialService {

    private final SocialRepository socialRepository;

    private final ModelMapper mapper;

    private final ProfileRepository profileRepository;

    public SocialServiceImpl(SocialRepository socialRepository, ModelMapper mapper, ProfileRepository profileRepository) {
        this.socialRepository = socialRepository;
        this.mapper = mapper;
        this.profileRepository = profileRepository;
    }

    @Override
    public ListData getAll(int page, int pageSize) {
        Page<SocialEntity> socialEntities = socialRepository.findAll(PageRequest.of(page, pageSize));
        return new ListData(true, "success", socialEntities.getContent(),
                new Pagination(socialEntities.getNumber(), socialEntities.getSize(), socialEntities.getTotalPages(),
                        (int) socialEntities.getTotalElements()));
    }

    @Override
    public Data getById(Long id) {
        // TODO Auto-generated method stub
        SocialEntity socialEntity = socialRepository.findById(id).orElseThrow(() -> new EntityNotFoundException());
        return new Data(true, "success", mapper.map(socialEntity, SocialDto.class));
    }

    @Override
    public Data add(SocialDto socialDto) {
        // TODO Auto-generated method stub
        SocialEntity user = mapper.map(socialDto, SocialEntity.class);
        return new Data(true, "success", mapper.map(socialRepository.save(user), SocialDto.class));
    }

    @Override
    public Data update(SocialDto socialDto, Long id) {
        // TODO Auto-generated method stub
        if (!socialRepository.existsById(socialDto.getId()))
            throw new EntityNotFoundException();
        SocialEntity socialRepo = socialRepository.getById(socialDto.getId());
        SocialEntity social = mapper.map(socialDto, SocialEntity.class);
        social.setId(socialRepo.getId());
        return new Data(true, "success", mapper.map(socialRepository.save(social), SocialDto.class));
    }

    @Override
    public Data delete(Long id) {
        // TODO Auto-generated method stub
        SocialEntity social = socialRepository.findById(id).orElseThrow(() -> new EntityNotFoundException());
        socialRepository.deleteById(id);
        return new Data(true, "success", mapper.map(social, SocialDto.class));
    }

    @Override
    public ListData getByProfileId(Pageable pageable, Long profileId) {
        profileRepository.findById(profileId).orElseThrow(() -> new EntityNotFoundException());
        Page<SocialEntity> pageSocial = socialRepository.findByProfileId(pageable, profileId);
        Pagination pagination = new Pagination(pageSocial.getNumber(), pageSocial.getSize(), pageSocial.getTotalPages(),
                (int) pageSocial.getTotalElements());
        List<SocialDto> linkDtos = pageSocial.stream().map(l -> mapper.map(l, SocialDto.class)).collect(Collectors.toList());
        return new ListData(true, "success", linkDtos, pagination);
    }

}
