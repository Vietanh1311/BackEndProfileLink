package com.example.profilelinks.model.dto;

import com.example.profilelinks.constants.TypeEnum;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class LinkDto {
  private Long id;
  private TypeEnum type;
  private String title;
  private String url;
  private String picture;
  @JsonProperty("profile_id")
  private Long profileId;
  @JsonProperty("click_count")
  private Long clickCount;
}
