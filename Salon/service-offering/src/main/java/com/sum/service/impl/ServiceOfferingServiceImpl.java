package com.sum.service.impl;

import com.sum.dto.CategoryDTO;
import com.sum.dto.SalonDTO;
import com.sum.dto.ServiceDTO;
import com.sum.model.ServiceOffering;
import com.sum.repository.ServiceOfferingRepository;
import com.sum.service.ServiceOfferingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ServiceOfferingServiceImpl implements ServiceOfferingService {
    private final ServiceOfferingRepository serviceOfferingRepository;
    @Override
    public ServiceOffering createService(SalonDTO salonSto, ServiceDTO serviceDTO, CategoryDTO categoryDTO) {

        ServiceOffering serviceOffering=new ServiceOffering();
        serviceOffering.setImage(serviceDTO.getImage());
        serviceOffering.setName(serviceDTO.getName());
        serviceOffering.setDescription(serviceDTO.getDescription());
        serviceOffering.setPrice(serviceDTO.getPrice());
        serviceOffering.setDuration(serviceDTO.getDuration());
        serviceOffering.setSalonId(salonSto.getId());
        serviceOffering.setCategoryId(categoryDTO.getId());
        return serviceOfferingRepository.save(serviceOffering);
    }

    @Override
    public ServiceOffering updateService(Long serviceId, ServiceOffering service) throws Exception {
        ServiceOffering serviceOffering=serviceOfferingRepository.findById(serviceId).orElse(null);
        if(serviceOffering==null)
        {
            throw new Exception("Service not exist with id");
        }
        ServiceOffering services=new ServiceOffering();
        serviceOffering.setImage(services.getImage());
        serviceOffering.setName(services.getName());
        serviceOffering.setDescription(services.getDescription());
        serviceOffering.setPrice(services.getPrice());
        serviceOffering.setDuration(services.getDuration());
    return serviceOfferingRepository.save(serviceOffering);
    }

    @Override
    public Set<ServiceOffering> getAllServicesBySalonId(Long salonId, Long categoryId) {
        Set<ServiceOffering> services=serviceOfferingRepository.findBySalonId(salonId);
        if(categoryId!=null)
        {
         services=services.stream().filter((service)->service.getCategoryId() !=null
         && service.getCategoryId().equals(categoryId)).collect(Collectors.toSet());
        }
        return services;
    }

    @Override
    public Set<ServiceOffering> getServicesByIds(Set<Long> ids) {
        List<ServiceOffering> services=serviceOfferingRepository.findAllById(ids);
        return new HashSet<>(services);
    }

    @Override
    public ServiceOffering getServiceById(Long id) throws Exception {
        ServiceOffering serviceOffering=serviceOfferingRepository.findById(id).orElse(null);
        if(serviceOffering==null)
        {
            throw new Exception("service not exist with id"+id);
        }
        return serviceOffering;
    }
}
