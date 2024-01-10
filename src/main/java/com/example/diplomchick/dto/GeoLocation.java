package com.example.diplomchick.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;


@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class GeoLocation {

    @JsonProperty("country")
    private String country;

    @JsonProperty("city")
    private String city;


}
