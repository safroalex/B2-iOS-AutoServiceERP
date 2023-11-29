package com.safroalex.AutoServiceERP.unit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safroalex.AutoServiceERP.model.Service;
import com.safroalex.AutoServiceERP.service.ServiceService;
import com.safroalex.AutoServiceERP.controller.ServiceController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class ServiceControllerTest {

    @Mock
    private ServiceService serviceService;

    @InjectMocks
    private ServiceController serviceController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(serviceController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testAddService() throws Exception {
        Service service = new Service();
        service.setName("Тестовая услуга");
        when(serviceService.saveService(any(Service.class))).thenReturn(service);

        mockMvc.perform(post("/api/services")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(service)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Тестовая услуга"));

        verify(serviceService, times(1)).saveService(any(Service.class));
    }

    @Test
    void testGetAllServices() throws Exception {
        List<Service> services = Arrays.asList(new Service(), new Service());
        when(serviceService.getAllServices()).thenReturn(services);

        mockMvc.perform(get("/api/services"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(services.size()));

        verify(serviceService, times(1)).getAllServices();
    }

    @Test
    void testUpdateService() throws Exception {
        Long serviceId = 1L;
        Service existingService = new Service();
        existingService.setId(serviceId);
        existingService.setName("Стандартное обслуживание");

        Service updatedService = new Service();
        updatedService.setName("Улучшенное обслуживание");

        when(serviceService.updateService(eq(serviceId), any(Service.class))).thenReturn(updatedService);

        mockMvc.perform(put("/api/services/" + serviceId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedService)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Улучшенное обслуживание"));

        verify(serviceService, times(1)).updateService(eq(serviceId), any(Service.class));
    }

    @Test
    void testDeleteService() throws Exception {
        Long serviceId = 1L;
        when(serviceService.deleteService(serviceId)).thenReturn(ResponseEntity.ok().build());

        mockMvc.perform(delete("/api/services/" + serviceId))
                .andExpect(status().isOk());

        verify(serviceService, times(1)).deleteService(serviceId);
    }
}
