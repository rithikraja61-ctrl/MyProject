package com.sum.Model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String image;

    @Column(nullable = false)
    private Long salonId;

    @Column(nullable = false)
    private String name;


}
