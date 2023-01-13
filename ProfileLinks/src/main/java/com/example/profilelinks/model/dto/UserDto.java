package com.example.profilelinks.model.dto;

import com.example.profilelinks.constants.RoleEnum;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long id;
    @NotBlank
    private String username;
    @NotBlank
    private String password;
    private String mail;
    private RoleEnum role;
    @JsonProperty("is_profile")
    private Boolean isProfile;
    @JsonProperty("is_update_role")
    private Boolean isUpgradeRole;
}
