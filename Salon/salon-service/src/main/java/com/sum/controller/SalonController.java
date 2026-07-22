package com.sum.controller;

import com.sum.mapper.SalonMapper;
import com.sum.model.Salon;
import com.sum.payload.dto.SalonDTO;
import com.sum.payload.dto.UserDTO;
import com.sum.service.SalonService;
import com.sum.service.impl.SalonServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/salons")
public class SalonController {
    private final SalonService salonService;

    //http://localhost:5002/api/salons
    @PostMapping
    public ResponseEntity<SalonDTO> createSalon(@RequestBody SalonDTO salonDTO) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        Salon salon = salonService.createSalon(salonDTO, userDTO);
        SalonDTO salonDTO1 = SalonMapper.toSalonDTO(salon);
        return ResponseEntity.ok(salonDTO1);
    }

    //http://localhost:5002/api/salons/1
    @PatchMapping("/{salonId}")
    public ResponseEntity<SalonDTO> updateSalon(@PathVariable("salonId") Long id, @RequestBody SalonDTO salonDTO) throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        Salon salon = salonService.updateSalon(salonDTO, userDTO, id);
        SalonDTO salonDTO1=SalonMapper.toSalonDTO(salon);
        return ResponseEntity.ok(salonDTO1);
    }
//http://localhost:5002/api/salons
    @GetMapping
    public ResponseEntity<List<SalonDTO>> getSalons() {
        List<Salon> salons = salonService.getAllSalons();
        List<SalonDTO> salonDTOS = salons.stream().map((salon) ->
        {
            SalonDTO salonDTO = SalonMapper.toSalonDTO(salon);
            return salonDTO;
        }).toList();
        return ResponseEntity.ok(salonDTOS);
    }
// http://localhost:5002/api/salons/1
    @GetMapping("/{salonId}")
    public ResponseEntity<SalonDTO> getSalonById(@PathVariable("salonId") Long id) throws Exception {
        Salon salon = salonService.getSalonById(id);
        SalonDTO salonDTO = SalonMapper.toSalonDTO(salon);
        return ResponseEntity.ok(salonDTO);
    }
    // http://localhost:5002/api/salons/search?city=Chennai
    @GetMapping("/search")
    public ResponseEntity<List<SalonDTO>> searchSalons(@RequestParam("city") String city) {
        List<Salon> salons = salonService.searchSalonByCity(city);
        List<SalonDTO> salonDTOS = salons.stream().map((salon) ->
        {
            SalonDTO salonDTO = SalonMapper.toSalonDTO(salon);
            return salonDTO;
        }).toList();
        return ResponseEntity.ok(salonDTOS);
    }
    @GetMapping("/owner")
    public ResponseEntity<SalonDTO> getSalonByOwnerId(@PathVariable("salonId") Long id) throws Exception {
        UserDTO userDTO=new UserDTO();
        userDTO.setId(1L);
        Salon salon = salonService.getSalonByOwnerId(userDTO.getId());
        SalonDTO salonDTO = SalonMapper.toSalonDTO(salon);
        return ResponseEntity.ok(salonDTO);
    }
}
