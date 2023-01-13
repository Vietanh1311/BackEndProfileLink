package com.example.profilelinks.controller;

import com.example.profilelinks.model.dto.SocialDto;
import com.example.profilelinks.model.response.Data;
import com.example.profilelinks.model.response.ListData;
import com.example.profilelinks.service.impl.SocialServiceImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("api/v1.0/social")
public class SocialController {

    SocialServiceImpl socialService;

    public SocialController(SocialServiceImpl socialService) {
        this.socialService = socialService;
    }

    @GetMapping()
    public ResponseEntity<ListData> getAll(@RequestParam("page") int page, @RequestParam("page-size") int pageSize) {
        return ResponseEntity.ok(socialService.getAll(page, pageSize));
    }
    @GetMapping("/{id}")
    public ResponseEntity<Data> getById(@PathVariable Long id) {
        return ResponseEntity.ok(socialService.getById(id));
    }
    @PostMapping("add")
    public ResponseEntity<Data> add(@RequestBody SocialDto socialDto) {
        return ResponseEntity.ok(socialService.add(socialDto));
    }
    @PutMapping("/{id}")
    public ResponseEntity<Data> update(@RequestBody SocialDto socialDto, @PathVariable Long id) {
        return ResponseEntity.ok(socialService.update(socialDto, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Data> delete(@PathVariable Long id) {
        return ResponseEntity.ok(socialService.delete(id));
    }

    @GetMapping("/get/{profileId}")
    public ResponseEntity<?> getByProfileId(
            @RequestParam("page") int page,
            @RequestParam("page-size") int pageSize,
            @PathVariable Long profileId
    ){
        return new ResponseEntity<>(socialService.getByProfileId(PageRequest.of(page,pageSize),profileId),HttpStatus.OK);
    }
}
