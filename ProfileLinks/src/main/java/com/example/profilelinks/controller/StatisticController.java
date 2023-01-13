package com.example.profilelinks.controller;

import com.example.profilelinks.model.dto.StatisticDto;
import com.example.profilelinks.model.response.Data;
import com.example.profilelinks.model.response.ListData;
import com.example.profilelinks.service.impl.StatisticServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("api/v1.0/statistic")
public class StatisticController {

    StatisticServiceImpl chartsService;

    public StatisticController(StatisticServiceImpl chartsService) {
        this.chartsService = chartsService;
    }

    @GetMapping()
    public ResponseEntity<ListData> getAll(@RequestParam int page, @RequestParam int pageSize) {
        return ResponseEntity.ok(chartsService.getAll(page, pageSize));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Data> getById(@PathVariable Long id) {
        return ResponseEntity.ok(chartsService.getById(id));
    }

    @PostMapping()
    public ResponseEntity<Data> add(@RequestBody StatisticDto statisticDto) {
        return ResponseEntity.ok(chartsService.add(statisticDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Data> update(@RequestBody StatisticDto statisticDto, @PathVariable Long id) {
        return ResponseEntity.ok(chartsService.update(statisticDto, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Data> delete(@PathVariable Long id) {
        return ResponseEntity.ok(chartsService.delete(id));
    }
    
    @GetMapping("/top-month")
    public ResponseEntity<?> getTopProfileToMonth(@RequestParam("page") int page, @RequestParam("page-size") int pageSize,@RequestParam("month") int month, @RequestParam("year") int year) {
        return new ResponseEntity<>(chartsService.getTopProfileToMonth(page, pageSize, month, year), HttpStatus.OK);
    }
}
