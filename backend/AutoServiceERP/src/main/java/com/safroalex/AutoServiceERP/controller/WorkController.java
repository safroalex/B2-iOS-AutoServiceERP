package com.safroalex.AutoServiceERP.controller;

import com.safroalex.AutoServiceERP.dto.MasterDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.safroalex.AutoServiceERP.model.Work;
import com.safroalex.AutoServiceERP.service.WorkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ParseException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

@RestController
@RequestMapping("/api/works")
public class WorkController {

    private final WorkService workService;

    private static final Logger logger = LoggerFactory.getLogger(WorkController.class);
    @Autowired
    public WorkController(WorkService workService) {
        this.workService = workService;
    }

    // Создать новую работу
    @PostMapping
    public ResponseEntity<Work> createWork(@RequestBody Work work) {
        Work newWork = workService.createWork(work);
        return ResponseEntity.ok(newWork);
    }

    // Получить список всех работ
    @GetMapping
    public List<Work> getAllWorks() {
        return workService.getAllWorks();
    }

    // Получить работу по ID
    @GetMapping("/{id}")
    public ResponseEntity<Work> getWorkById(@PathVariable Long id) {
        Work work = workService.getWorkById(id);
        return ResponseEntity.ok(work);
    }

    // Обновить работу
    @PutMapping("/{id}")
    public ResponseEntity<Work> updateWork(@PathVariable Long id, @RequestBody Work workDetails) {
        Work updatedWork = workService.updateWork(id, workDetails);
        return ResponseEntity.ok(updatedWork);
    }

    // Удалить работу
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteWork(@PathVariable Long id) {
        workService.deleteWork(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/total-cost")
    public ResponseEntity<?> getTotalCost(
            @RequestParam(value = "fromDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime fromDate,
            @RequestParam(value = "toDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime toDate) {
        try {
            Double totalCost = workService.getTotalCost(fromDate, toDate);
            Map<String, Double> response = Collections.singletonMap("totalCost", totalCost);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error retrieving total cost", e);
            return ResponseEntity.badRequest().body("Ошибка при получении данных");
        }
    }



    @GetMapping("/top-masters")
    public ResponseEntity<?> getTopMasters(
            @RequestParam(value = "fromDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime fromDate,
            @RequestParam(value = "toDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime toDate) {
        try {
            List<MasterDTO> topMasters = workService.getTopMasters(fromDate, toDate);
            return ResponseEntity.ok(topMasters);
        } catch (Exception e) {
            logger.error("Error retrieving top masters", e);
            return ResponseEntity.badRequest().body("Ошибка при получении данных");
        }
    }


}
