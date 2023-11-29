package com.safroalex.AutoServiceERP.service;

import com.safroalex.AutoServiceERP.exception.ResourceNotFoundException;
import com.safroalex.AutoServiceERP.model.Service;
import com.safroalex.AutoServiceERP.repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.util.List;

@org.springframework.stereotype.Service
public class ServiceService {
    private final ServiceRepository serviceRepository;

    @Autowired
    public ServiceService(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    public Service saveService(Service service) {
        return serviceRepository.save(service);
    }

    public List<Service> getAllServices() {
        return serviceRepository.findAll();
    }

    public Service updateService(Long id, Service serviceDetails) {
        Service service = serviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Service not found with id : " + id));
        service.setName(serviceDetails.getName());
        service.setCostOur(serviceDetails.getCostOur());
        service.setCostForeign(serviceDetails.getCostForeign());
        return serviceRepository.save(service);
    }

    public ResponseEntity<?> deleteService(Long id) {
        Service service = serviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Service not found with id : " + id));
        serviceRepository.delete(service);
        return ResponseEntity.ok().build();
    }
}
