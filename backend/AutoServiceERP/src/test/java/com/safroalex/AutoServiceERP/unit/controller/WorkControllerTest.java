package com.safroalex.AutoServiceERP.unit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safroalex.AutoServiceERP.model.*;
import com.safroalex.AutoServiceERP.service.WorkService;
import com.safroalex.AutoServiceERP.controller.WorkController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;

import java.text.SimpleDateFormat;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;


import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class WorkControllerTest {

    @Mock
    private WorkService workService;

    @InjectMocks
    private WorkController workController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(workController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testCreateWork() throws Exception {
        // Создаём тестовые экземпляры связанных сущностей
        Master testMaster = new Master();
        testMaster.setId(1L); // Пример идентификатора мастера
        testMaster.setName("Иван Иванович");

        Car testCar = new Car();
        testCar.setId(1L); // Пример идентификатора автомобиля
        // Дополнительные поля для Car

        Service testService = new Service();
        testService.setId(1L); // Пример идентификатора услуги
        // Дополнительные поля для Service

        // Создаём экземпляр Work с тестовыми данными
        Work work = new Work();
        work.setDateWork(new Date());
        work.setMaster(testMaster);
        work.setCar(testCar);
        work.setService(testService);

        // Настройка моков
        when(workService.createWork(any(Work.class))).thenReturn(work);

        // Выполнение запроса POST и проверка результата
        mockMvc.perform(post("/api/works")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(work)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.master.id").value(work.getMaster().getId()))
                .andExpect(jsonPath("$.car.id").value(work.getCar().getId()))
                .andExpect(jsonPath("$.service.id").value(work.getService().getId()));

        // Проверка, что метод createWork был вызван
        verify(workService, times(1)).createWork(any(Work.class));
    }

    @Test
    void testGetAllWorks() throws Exception {
        // Создаем список работ
        List<Work> works = new ArrayList<>();

        // Добавляем тестовые работы в список
        for (int i = 1; i <= 3; i++) {
            Work work = new Work();
            work.setId((long) i);
            work.setDateWork(new Date());
            work.setMaster(new Master());
            work.setCar(new Car());
            work.setService(new Service());
            works.add(work);
        }

        // Настройка моков
        when(workService.getAllWorks()).thenReturn(works);

        // Выполнение запроса GET и проверка результата
        mockMvc.perform(get("/api/works"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3))) // Проверяем, что возвращается 3 работы
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[2].id", is(3)));

        // Проверка, что метод getAllWorks был вызван
        verify(workService, times(1)).getAllWorks();
    }

    @Test
    void testGetWorkById() throws Exception {
        // Создаем тестовый объект Work
        Long workId = 1L;
        Work testWork = new Work();
        testWork.setId(workId);
        testWork.setDateWork(new Date());
        testWork.setMaster(new Master()); // Настройте тестового мастера
        testWork.setCar(new Car());       // Настройте тестовый автомобиль
        testWork.setService(new Service()); // Настройте тестовую услугу

        // Настройка моков
        when(workService.getWorkById(workId)).thenReturn(testWork);

        // Выполнение запроса GET и проверка результата
        mockMvc.perform(get("/api/works/" + workId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(workId.intValue())))
                .andExpect(jsonPath("$.master", notNullValue())) // Проверяем, что данные мастера есть
                .andExpect(jsonPath("$.car", notNullValue()))    // Проверяем, что данные автомобиля есть
                .andExpect(jsonPath("$.service", notNullValue())); // Проверяем, что данные услуги есть

        // Проверка, что метод getWorkById был вызван
        verify(workService, times(1)).getWorkById(workId);
    }

    @Test
    void testUpdateWork() throws Exception {
        Work updatedWork = new Work();
        updatedWork.setId(1L);
        Date newDate = new Date();
        updatedWork.setDateWork(newDate);

        when(workService.updateWork(anyLong(), any(Work.class))).thenReturn(updatedWork);

        // Установка часового пояса для форматирования даты
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        String formattedDate = dateFormat.format(newDate);

        mockMvc.perform(put("/api/works/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedWork)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.dateWork").value(formattedDate));
    }

    @Test
    void testDeleteWork() throws Exception {
        Long workId = 1L; // ID работы, которую нужно удалить

        // Настройка мок-объекта сервиса, чтобы он не выбрасывал исключение при удалении
        doNothing().when(workService).deleteWork(workId);

        // Выполнение запроса на удаление и проверка результатов
        mockMvc.perform(delete("/api/works/{id}", workId))
                .andExpect(status().isOk());

        // Проверка, что метод удаления был вызван
        verify(workService, times(1)).deleteWork(workId);
    }

}
