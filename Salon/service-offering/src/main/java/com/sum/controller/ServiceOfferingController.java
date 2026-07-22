package com.sum.controller;

import com.sum.model.ServiceOffering;
import com.sum.service.ServiceOfferingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.lang.model.element.VariableElement;
import java.util.Set;

@RestController
@RequestMapping("/api/services")
@RequiredArgsConstructor
public class ServiceOfferingController
{
private final ServiceOfferingService serviceOfferingService;

@GetMapping("/salon/{salonId}")
public ResponseEntity<Set<ServiceOffering>> getServicesBySalonId(
        @PathVariable Long salonId,
        @RequestParam(required = false) Long categoryId)
{
    Set<ServiceOffering> serviceOfferings = serviceOfferingService.getAllServicesBySalonId(salonId, categoryId);
    return ResponseEntity.ok(serviceOfferings);
}

@GetMapping("/{id}")
public ResponseEntity<ServiceOffering> getServiceById(@PathVariable Long id) throws Exception
{
    ServiceOffering serviceOffering = serviceOfferingService.getServiceById(id);
    return ResponseEntity.ok(serviceOffering);

}
    @GetMapping("/list/{ids}")
    public ResponseEntity<Set<ServiceOffering>> getServicesByIds(
            @PathVariable Set<Long> ids)
    {
        Set<ServiceOffering> serviceOfferings = serviceOfferingService.getServicesByIds(ids);
        return ResponseEntity.ok(serviceOfferings);
    }
}
