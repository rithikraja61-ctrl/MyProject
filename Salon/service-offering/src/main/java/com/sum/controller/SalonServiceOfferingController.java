package com.sum.controller;

import com.sum.dto.CategoryDTO;
import com.sum.dto.SalonDTO;
import com.sum.dto.ServiceDTO;
import com.sum.model.ServiceOffering;
import com.sum.service.ServiceOfferingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/service-offering/salon-owner")
public class SalonServiceOfferingController {

    private final ServiceOfferingService serviceOfferingService;


    @PostMapping
    public ResponseEntity<ServiceOffering> createService(
            @RequestBody ServiceDTO serviceDTO

    )
    {
        SalonDTO salonDTO=new SalonDTO();
        salonDTO.setId(1L);
        CategoryDTO categoryDTO=new CategoryDTO();
        categoryDTO.setId(serviceDTO.getCategoryId());

        ServiceOffering serviceOfferings = serviceOfferingService.createService(salonDTO,serviceDTO, categoryDTO);
        return ResponseEntity.ok(serviceOfferings);
    }


    public ResponseEntity<ServiceOffering> updateService(
            @PathVariable Long id,
            @RequestBody ServiceOffering serviceOffering

    ) throws Exception {
        SalonDTO salonDTO=new SalonDTO();
        salonDTO.setId(1L);
        CategoryDTO categoryDTO=new CategoryDTO();
        categoryDTO.setId(1L);

        ServiceOffering serviceOfferings = serviceOfferingService.updateService(id,serviceOffering);
        return ResponseEntity.ok(serviceOffering);
    }
}
