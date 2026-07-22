package com.sum.payload.dto;

import lombok.Data;

import java.time.LocalTime;
import java.util.List;
@Data
public class SalonDTO
{
        private Long id;

        private String  name;

        private List<String> image;

        private String address;

        private String phone;

        private String email;

        private String city;

        private Long OwnerId;

        private LocalTime openTime;

        private LocalTime closeTime;

    }
