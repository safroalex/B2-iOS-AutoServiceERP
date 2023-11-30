package com.safroalex.AutoServiceERP.integration.controller;

import com.safroalex.AutoServiceERP.model.Service;
import com.safroalex.AutoServiceERP.repository.ServiceRepository;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
public class ServiceControllerIntegrationTest {

    @Container
    public static PostgreSQLContainer<?> postgresqlContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void postgresqlProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresqlContainer::getJdbcUrl);
        registry.add("spring.datasource.password", postgresqlContainer::getPassword);
        registry.add("spring.datasource.username", postgresqlContainer::getUsername);
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ServiceRepository serviceRepository;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testAddService() throws Exception {
        Service service = new Service();
        service.setName("Тестовая услуга");
        service.setCostOur(500.0);
        service.setCostForeign(1000.0);

        mockMvc.perform(post("/api/services")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(service)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Тестовая услуга"));

        // Проверяем, что услуга сохранена в БД
        Optional<Service> savedService = serviceRepository.findByName("Тестовая услуга");
        assertTrue(savedService.isPresent());
        assertEquals("Тестовая услуга", savedService.get().getName());
    }

    @Test
    void testGetAllServices() throws Exception {
        // Предполагаем, что в базе данных уже есть некоторые услуги
        mockMvc.perform(get("/api/services"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$[0].name", is(notNullValue())));
    }

    @Test
    void testUpdateService() throws Exception {
        // Создаем новую услугу для обновления
        Service service = new Service();
        service.setName("Тестовая услуга");
        service.setCostOur(500.0);
        service.setCostForeign(1000.0);
        Service savedService = serviceRepository.save(service);

        // Обновляем данные услуги
        Service updatedInfo = new Service();
        updatedInfo.setName("Обновленная услуга");
        updatedInfo.setCostOur(600.0);
        updatedInfo.setCostForeign(1100.0);

        mockMvc.perform(put("/api/services/" + savedService.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedInfo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Обновленная услуга"));
    }

    @Test
    void testDeleteService() throws Exception {
        // Создаем новую услугу для удаления
        Service service = new Service();
        service.setName("Услуга для удаления");
        service.setCostOur(300.0);
        service.setCostForeign(700.0);
        Service savedService = serviceRepository.save(service);

        // Удаляем услугу
        mockMvc.perform(delete("/api/services/" + savedService.getId()))
                .andExpect(status().isOk());

        // Проверяем, что услуга больше не существует в базе данных
        Optional<Service> deletedService = serviceRepository.findById(savedService.getId());
        assertTrue(deletedService.isEmpty());
    }
}