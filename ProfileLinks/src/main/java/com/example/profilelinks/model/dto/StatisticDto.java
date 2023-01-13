package com.example.profilelinks.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StatisticDto {
    private Long id;
    @JsonProperty("click_count")
    private Long clickCount;
    @JsonProperty("profile_id")
    private Integer profileId;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date;
    @JsonProperty("avatar_link")
    private String avatarLink;
    private String fullname;
    @JsonProperty("short_bio")
    private String shortBio;
}
