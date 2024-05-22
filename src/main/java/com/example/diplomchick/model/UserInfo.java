package com.example.diplomchick.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class UserInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "coordinates_id")
    private Coordinates coordinates;
    private String country;
    private String city;
    private String ip;
    private String organization;
    private String asNumber;

    private LocalDateTime date;

    private String email;

}
