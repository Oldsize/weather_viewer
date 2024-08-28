package com.example.weather_project.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Entity
@NoArgsConstructor
@Getter
@Setter
@ToString
@Table(schema = "default", name = "locations")
public class Location {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "Name")
    private String name;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "UserId")
    private User userId;
    @Column(name = "Latitude")
    private BigDecimal latitude;
    @Column(name = "Longitude")
    private BigDecimal longitude;
    @Version
    private Long version;

    public Location(String name, User userId, BigDecimal latitude, BigDecimal longitude) {
        this.name = name;
        this.userId = userId;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Location(String name) {
        this.name = name;
    }
}