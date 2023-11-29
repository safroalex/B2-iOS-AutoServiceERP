package com.safroalex.AutoServiceERP.controller;

import com.safroalex.AutoServiceERP.model.Service;
import com.safroalex.AutoServiceERP.service.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/services")
public class ServiceController {
    private final ServiceService serviceService;

    @Autowired
    public ServiceController(ServiceService serviceService) {
        this.serviceService = serviceService;
    }

    @PostMapping
    public ResponseEntity<Service> addService(@RequestBody Service service) {
        Service savedService = serviceService.saveService(service);
        return ResponseEntity.ok(savedService);
    }

    @GetMapping
    public List<Service> getAllServices() {
        return serviceService.getAllServices();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Service> updateService(@PathVariable(value = "id") Long serviceId,
                                                       @RequestBody Service serviceDetails) {
        Service updatedService = serviceService.updateService(serviceId, serviceDetails);
        return ResponseEntity.ok(updatedService);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteService(@PathVariable(value = "id") Long serviceId) {
        return serviceService.deleteService(serviceId);
    }
}
