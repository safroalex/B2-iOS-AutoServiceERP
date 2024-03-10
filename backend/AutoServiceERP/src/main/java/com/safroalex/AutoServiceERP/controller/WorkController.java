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
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

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
            @RequestParam(value = "fromDate", required = false) String fromDateStr,
            @RequestParam(value = "toDate", required = false) String toDateStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");

        OffsetDateTime fromDate = null;
        OffsetDateTime toDate = null;

        try {
            if (fromDateStr != null) {
                fromDate = OffsetDateTime.parse(fromDateStr, formatter);
            }
            if (toDateStr != null) {
                toDate = OffsetDateTime.parse(toDateStr, formatter);
            }

            // Преобразуйте OffsetDateTime обратно в Date, если ваш WorkService ожидает Date
            // Или обновите WorkService, чтобы работать с OffsetDateTime напрямую

            // Предположим, что ваш WorkService уже обновлен для работы с OffsetDateTime
            return ResponseEntity.ok(workService.getTotalCost(fromDate, toDate));

        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().body("Неверный формат даты: " + e.getMessage());
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
