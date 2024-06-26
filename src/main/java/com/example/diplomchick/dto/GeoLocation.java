package com.example.diplomchick.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;


@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class GeoLocation {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    @JsonProperty("country")
    private String country;

    @JsonProperty("city")
    private String city;

    @JsonProperty("lat")
    private double latitude;

    @JsonProperty("lon")
    private double longitude;

    @JsonProperty("query")
    private String ip;

    @JsonProperty("org")
    private String organization;

    @JsonProperty("as")
    private String asNumber;

}
