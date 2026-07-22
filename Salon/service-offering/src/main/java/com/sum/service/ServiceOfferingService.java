package com.sum.service;

import com.sum.dto.CategoryDTO;
import com.sum.dto.SalonDTO;
import com.sum.dto.ServiceDTO;
import com.sum.model.ServiceOffering;

import java.security.Provider;
import java.util.Set;

public interface ServiceOfferingService {
 ServiceOffering createService(SalonDTO salonSto,
                               ServiceDTO serviceDTO,
                               CategoryDTO categoryDTO);

 ServiceOffering updateService(Long serviceId,
                              ServiceOffering service) throws Exception;

 Set<ServiceOffering> getAllServicesBySalonId(Long salonId,Long categoryId);

 Set<ServiceOffering> getServicesByIds(Set<Long> ids);

 ServiceOffering getServiceById(Long id) throws Exception;
}
