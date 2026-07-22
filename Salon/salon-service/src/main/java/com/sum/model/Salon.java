package com.sum.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import org.springframework.cglib.core.Local;

import java.time.LocalTime;
import java.util.List;

@Entity
@Data
public class Salon {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String  name;

    @ElementCollection
    private List<String> image;

    @Column(nullable = false)
    private String address;

    @Column(nullable =false)
    private String phone;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private Long OwnerId;

    @Column(nullable = false)
    private LocalTime openTime;

    @Column(nullable = false)
    private LocalTime closeTime;

}
