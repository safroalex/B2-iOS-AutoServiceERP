package com.safroalex.AutoServiceERP.integration.controller;

import com.safroalex.AutoServiceERP.model.Car;
import com.safroalex.AutoServiceERP.model.Master;
import com.safroalex.AutoServiceERP.model.Service;
import com.safroalex.AutoServiceERP.model.Work;
import com.safroalex.AutoServiceERP.repository.CarRepository;
import com.safroalex.AutoServiceERP.repository.MasterRepository;
import com.safroalex.AutoServiceERP.repository.ServiceRepository;
import com.safroalex.AutoServiceERP.repository.WorkRepository;
import com.safroalex.AutoServiceERP.service.WorkService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.containers.PostgreSQLContainer;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.*;
@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@ActiveProfiles("test")
@Transactional
public class WorkControllerIntegrationTest {

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
    private WorkService workService;

    @Autowired
    private MasterRepository masterRepository;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private WorkRepository workRepository;
    @BeforeEach
    void setUp() throws Exception {
        OffsetDateTime workDate1 = OffsetDateTime.of(2023, 1, 15, 12, 0, 0, 0, ZoneOffset.UTC); // Дата внутри диапазона
        OffsetDateTime workDate2 = OffsetDateTime.of(2023, 1, 20, 12, 0, 0, 0, ZoneOffset.UTC); // Еще одна дата внутри диапазона

        // Создание и сохранение мастеров
        Master master1 = new Master();
        master1.setName("Иван Иванович");
        masterRepository.save(master1);

        Master master2 = new Master();
        master2.setName("Петр Петрович");
        masterRepository.save(master2);

        Master master3 = new Master();
        master3.setName("Алексей Алексеевич");
        masterRepository.save(master3);

        Master master4 = new Master();
        master4.setName("Сергей Сергеевич");
        masterRepository.save(master4);

        Master master5 = new Master();
        master5.setName("Николай Николаевич");
        masterRepository.save(master5);

        Master master6 = new Master();
        master6.setName("Владимир Владимирович");
        masterRepository.save(master6);

        // Создание и сохранение услуги
        Service service = new Service();
        service.setName("Тестовая услуга");
        service.setCostOur(500.0);
        service.setCostForeign(1000.0);
        serviceRepository.save(service);

        // Создание и сохранение автомобилей
        Car car1 = new Car();
        car1.setNum("XYZ 123");
        car1.setColor("Синий");
        car1.setMark("Toyota");
        car1.setForeign(true);
        carRepository.save(car1);

        Car car2 = new Car();
        car2.setNum("RUS 456");
        car2.setColor("Красный");
        car2.setMark("Lada");
        car2.setForeign(false);
        carRepository.save(car2);

        // Создание и сохранение работ через функцию createWork
        createWork(master2, car1, service, workDate1);
        createWork(master2, car2, service, workDate1);
        createWork(master2, car1, service, workDate2);
        createWork(master1, car2, service, workDate2);

        createWork(master3, car1, service, workDate1);
        createWork(master4, car2, service, workDate2);
        createWork(master5, car1, service, workDate1);
        createWork(master6, car2, service, workDate2);
    }

    private void createWork(Master master, Car car, Service service, OffsetDateTime workDate) {
        Work work = new Work();
        work.setDateWork(workDate);
        work.setMaster(master);
        work.setCar(car);
        work.setService(service);
        workRepository.save(work);
    }


    @Test
    void getTotalCostForPeriodTest() throws Exception {
        mockMvc.perform(get("/api/works/total-cost")
                        .param("fromDate", "2023-01-01T00:00:00+00:00")
                        .param("toDate", "2023-01-31T23:59:59+00:00")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("6000.0"));
    }

    @Test
    void getTopMastersTest() throws Exception {
        String fromDate = "2023-01-01T00:00:00+00:00";
        String toDate = "2023-01-31T23:59:59+00:00";

        mockMvc.perform(get("/api/works/top-masters")
                        .param("fromDate", fromDate)
                        .param("toDate", toDate)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(5))) // Проверяем, что вернулось 5 мастеров
                .andExpect(jsonPath("$[0].name", is("Петр Петрович"))) // Проверяем имя мастера, который выполнил наибольшее количество работ
                .andExpect(jsonPath("$[0].numberOfWorks", is(greaterThan(2)))); // Проверяем, что у мастера более 2 работ
    }
}