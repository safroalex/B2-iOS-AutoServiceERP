package com.safroalex.AutoServiceERP.integration.controller;

import com.safroalex.AutoServiceERP.model.Car;
import com.safroalex.AutoServiceERP.repository.CarRepository;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
public class CarControllerIntegrationTest {

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
    private CarRepository carRepository;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testAddCar() throws Exception {
        Car car = new Car();
        car.setNum("XYZ 123");
        car.setColor("Синий");
        car.setMark("Toyota");
        car.setForeign(true);

        mockMvc.perform(post("/api/cars")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(car)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.num").value("XYZ 123"));

        // Проверяем, что автомобиль сохранен в БД
        Optional<Car> savedCar = carRepository.findByNum("XYZ 123");
        assertTrue(savedCar.isPresent());
        assertEquals("XYZ 123", savedCar.get().getNum());
    }

    @Test
    void testAddExistingCar() throws Exception {
        // Создаем автомобиль
        Car car = new Car();
        car.setNum("XYZ 123");
        car.setColor("Синий");
        car.setMark("Toyota");
        car.setForeign(true);
        carRepository.save(car);

        // Пытаемся создать тот же автомобиль еще раз
        mockMvc.perform(post("/api/cars")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(car)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Car with number " + car.getNum() + " already exists"));

        // Очистка репозитория после теста
        carRepository.delete(car);
    }


    @Test
    void testGetAllCars() throws Exception {
        // Предполагаем, что в базе данных уже есть некоторые автомобили
        mockMvc.perform(get("/api/cars"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$[0].num", is(notNullValue())));
    }

    @Test
    void testUpdateCar() throws Exception {
        // Создаем новый автомобиль для обновления
        Car car = new Car();
        car.setNum("XYZ 123");
        car.setColor("Синий");
        car.setMark("Toyota");
        car.setForeign(true);
        Car savedCar = carRepository.save(car);

        // Обновляем данные автомобиля
        Car updatedInfo = new Car();
        updatedInfo.setNum("XYZ 456");
        updatedInfo.setColor("Красный");
        updatedInfo.setMark("Toyota");
        updatedInfo.setForeign(true);

        mockMvc.perform(put("/api/cars/" + savedCar.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedInfo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.num").value("XYZ 456"));

        // Проверяем, что данные автомобиля обновлены в БД
        Optional<Car> updatedCar = carRepository.findById(savedCar.getId());
        assertTrue(updatedCar.isPresent());
        assertEquals("XYZ 456", updatedCar.get().getNum());
    }

    @Test
    void testDeleteCar() throws Exception {
        // Создаем новый автомобиль для удаления
        Car car = new Car();
        car.setNum("XYZ 789");
        car.setColor("Зеленый");
        car.setMark("Toyota");
        car.setForeign(false);
        Car savedCar = carRepository.save(car);

        // Удаляем автомобиль
        mockMvc.perform(delete("/api/cars/" + savedCar.getId()))
                .andExpect(status().isOk());

        // Проверяем, что автомобиль больше не существует в базе данных
        Optional<Car> deletedCar = carRepository.findById(savedCar.getId());
        assertTrue(deletedCar.isEmpty());
    }

}
