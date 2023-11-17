package com.safroalex.AutoServiceERP.integration.controller;

import com.safroalex.AutoServiceERP.repository.MasterRepository;
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


import com.safroalex.AutoServiceERP.model.Master;

import org.junit.jupiter.api.Test;

import org.springframework.http.MediaType;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
public class MasterControllerIntegrationTest {

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
    private MasterRepository masterRepository;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testAddMaster() throws Exception {
        Master master = new Master();
        master.setName("Иван Иванович");

        mockMvc.perform(post("/api/masters")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(master)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Иван Иванович"));

        // После того, как запрос к API выполнен, проверяем, что данные были сохранены в базе данных
//        Optional<Master> savedMaster = masterRepository.findByName("Иван Иванович");
//        assertTrue(savedMaster.isPresent());
//        assertEquals("Иван Иванович", savedMaster.get().getName());
    }
}