package com.example.profilelinks.model.entity;

import com.example.profilelinks.model.dto.SocialDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "socials")
public class SocialEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @JsonProperty("social_name")
    @Column(name = "social_name")
    private String name;
    @JsonProperty("social_icon")
    @Column(name = "social_icon")
    private String icon;
    private String links;
    @JsonProperty("profile_id")
    @Column(name = "profile_id")
    private Long profileId;
    @JsonProperty("click_count")
    @Column(name = "click_count")
    private Long clickCount;

    public SocialEntity setValueFromDto(SocialDto socialDto) {
        this.id = socialDto.getId();
        this.name = socialDto.getName();
        this.icon = socialDto.getIcon();
        this.links = socialDto.getLinks();
        this.profileId = socialDto.getProfileId();
        this.clickCount = socialDto.getClickCount();

        return this;
    }
}
