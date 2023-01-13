package com.example.profilelinks.model.entity;

import com.example.profilelinks.model.dto.StatisticDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "statistics")
@AllArgsConstructor
@NoArgsConstructor
public class StatisticEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JsonProperty("click_count")
    @Column(name = "click_count")
    private Long clickCount;
    @JsonProperty("profile_id")
    @Column(name = "profile_id")
    private Integer profileId;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date;

    public StatisticEntity setValueByDto(StatisticDto statisticDto) {
        this.id = statisticDto.getId();
        this.clickCount = statisticDto.getClickCount();
        this.profileId = statisticDto.getProfileId();
        this.date = statisticDto.getDate();

        return this;
    }
}
