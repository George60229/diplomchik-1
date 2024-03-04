package com.example.diplomchick.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserInfo {
    private Coordinates coordinates;


    private String country;

    private String city;
    private String ip;


    private String organization;


    private String as;

}
