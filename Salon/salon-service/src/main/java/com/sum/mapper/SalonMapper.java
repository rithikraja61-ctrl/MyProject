package com.sum.mapper;

import com.sum.model.Salon;
import com.sum.payload.dto.SalonDTO;

public class SalonMapper {
    public static SalonDTO toSalonDTO(Salon salon) {
        SalonDTO salonDTO = new SalonDTO();
        salonDTO.setId(salon.getId());
        salonDTO.setName(salon.getName());
        salonDTO.setAddress(salon.getAddress());
        salonDTO.setCity(salon.getCity());
        salonDTO.setEmail(salon.getEmail());
        salonDTO.setPhone(salon.getPhone());
        salonDTO.setOpenTime(salon.getOpenTime());
        salonDTO.setCloseTime(salon.getCloseTime());
        return salonDTO;
    }
}
