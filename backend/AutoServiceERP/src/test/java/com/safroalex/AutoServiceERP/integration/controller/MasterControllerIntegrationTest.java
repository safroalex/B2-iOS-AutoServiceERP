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

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.notNullValue;

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

        Optional<Master> savedMaster = masterRepository.findByName("Иван Иванович");
        assertTrue(savedMaster.isPresent());
        assertEquals("Иван Иванович", savedMaster.get().getName());
    }

    @Test
    void testGetAllMasters() throws Exception {
        // Добавляем мастера в базу данных
        Master testMaster = new Master();
        testMaster.setName("Петр Петрович");
        masterRepository.save(testMaster);

        // Теперь выполняем тест
        mockMvc.perform(get("/api/masters"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$[0].name", is("Петр Петрович")));
    }


    @Test
    void testUpdateMaster() throws Exception {
        // Создаем нового мастера для обновления
        Master master = new Master();
        master.setName("Петр Петрович");
        Master savedMaster = masterRepository.save(master);

        // Обновляем данные мастера
        Master updatedInfo = new Master();
        updatedInfo.setName("Петр Измененный");

        mockMvc.perform(put("/api/masters/" + savedMaster.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedInfo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Петр Измененный"));
    }

    @Test
    void testDeleteMaster() throws Exception {
        // Создаем нового мастера для удаления
        Master master = new Master();
        master.setName("Мастер для удаления");
        Master savedMaster = masterRepository.save(master);

        // Удаляем мастера
        mockMvc.perform(delete("/api/masters/" + savedMaster.getId()))
                .andExpect(status().isOk());

        // Проверяем, что мастер больше не существует в базе данных
        Optional<Master> deletedMaster = masterRepository.findById(savedMaster.getId());
        assertTrue(deletedMaster.isEmpty());
    }

}