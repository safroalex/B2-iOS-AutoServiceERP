package com.safroalex.AutoServiceERP.unit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safroalex.AutoServiceERP.model.Master;
import com.safroalex.AutoServiceERP.service.MasterService;
import com.safroalex.AutoServiceERP.сontroller.MasterController;
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
public class MasterControllerTest {

    @Mock
    private MasterService masterService;

    @InjectMocks
    private MasterController masterController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(masterController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testAddMaster() throws Exception {
        Master master = new Master();
        master.setName("Иван Иванович");
        when(masterService.saveMaster(any(Master.class))).thenReturn(master);

        mockMvc.perform(post("/api/masters")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(master)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Иван Иванович"));

        verify(masterService, times(1)).saveMaster(any(Master.class));
    }

    @Test
    void testGetAllMasters() throws Exception {
        List<Master> masters = Arrays.asList(new Master(), new Master());
        when(masterService.getAllMasters()).thenReturn(masters);

        mockMvc.perform(get("/api/masters"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(masters.size()));

        verify(masterService, times(1)).getAllMasters();
    }

    @Test
    void testUpdateMaster() throws Exception {
        Long masterId = 1L;
        Master existingMaster = new Master();
        existingMaster.setId(masterId);
        existingMaster.setName("Иван");

        Master updatedMaster = new Master();
        updatedMaster.setName("Иван Измененный");

        when(masterService.updateMaster(eq(masterId), any(Master.class))).thenReturn(updatedMaster);

        mockMvc.perform(put("/api/masters/" + masterId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedMaster)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Иван Измененный"));

        verify(masterService, times(1)).updateMaster(eq(masterId), any(Master.class));
    }

    @Test
    void testDeleteMaster() throws Exception {
        Long masterId = 1L;
        when(masterService.deleteMaster(masterId)).thenReturn(ResponseEntity.ok().build());

        mockMvc.perform(delete("/api/masters/" + masterId))
                .andExpect(status().isOk());

        verify(masterService, times(1)).deleteMaster(masterId);
    }
}
