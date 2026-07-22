package com.sum.service.impl;

import com.sum.model.Salon;
import com.sum.payload.dto.SalonDTO;
import com.sum.payload.dto.UserDTO;
import com.sum.repository.SalonRepository;
import com.sum.service.SalonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class SalonServiceImpl implements SalonService {
    private final SalonRepository salonRepository;
    @Override
    public Salon createSalon(SalonDTO req, UserDTO user) {
        Salon salon=new Salon();
        salon.setAddress(req.getAddress());
        salon.setCity(req.getCity());
        salon.setEmail(req.getEmail());
        salon.setName(req.getName());
        salon.setPhone(req.getPhone());
        salon.setOwnerId(user.getId());
        salon.setOpenTime(req.getOpenTime());
        salon.setCloseTime(req.getCloseTime());
        return salonRepository.save(salon);
    }

    @Override
    public Salon updateSalon(SalonDTO salon, UserDTO user, Long salonId) throws Exception {
        Salon existingSalon=salonRepository.findById(salonId).orElse(null);
        if(existingSalon!=null&&existingSalon.getOwnerId().equals(user.getId())){
            existingSalon.setAddress(salon.getAddress());
            existingSalon.setCity(salon.getCity());
            existingSalon.setEmail(salon.getEmail());
            existingSalon.setName(salon.getName());
            existingSalon.setPhone(salon.getPhone());
            existingSalon.setOpenTime(salon.getOpenTime());
            existingSalon.setCloseTime(salon.getCloseTime());
            return salonRepository.save(existingSalon);
        }
        else {
            throw new Exception("Salon not found or you are not the owner");
        }
    }

    @Override
    public List<Salon> getAllSalons() {
        return salonRepository.findAll();
    }

    @Override
    public Salon getSalonById(Long salonId) throws Exception{
        Salon salon = salonRepository.findById(salonId).orElse(null);
        if (salon == null) {
            throw new Exception("Salon not found");
        }
        return salon;
    }

    @Override
    public Salon getSalonByOwnerId(Long ownerId) {
        return salonRepository.findByOwnerId(ownerId);
    }

    @Override
    public List<Salon> searchSalonByCity(String city) {
        return salonRepository.searchByCity(city);
    }
}
