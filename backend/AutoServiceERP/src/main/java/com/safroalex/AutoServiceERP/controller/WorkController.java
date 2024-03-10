package com.safroalex.AutoServiceERP.controller;

import com.safroalex.AutoServiceERP.dto.MasterDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.safroalex.AutoServiceERP.model.Work;
import com.safroalex.AutoServiceERP.service.WorkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ParseException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
        Date fromDate = null;
        Date toDate = null;
        try {
            if (fromDateStr != null) {
                fromDate = dateFormat.parse(fromDateStr);
            }
            if (toDateStr != null) {
                toDate = dateFormat.parse(toDateStr);
            }
            // ошибка и автоматического преобразования типов Spring.
            return ResponseEntity.ok(workService.getTotalCost(fromDate, toDate));
        } catch (ParseException e) {
            // Обработка ошибки парсинга даты
            return ResponseEntity.badRequest().body("Неверный формат даты");
        } catch (java.text.ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/top-masters")
    public ResponseEntity<?> getTopMasters(
            @RequestParam(value = "fromDate", required = false) String fromDateStr,
            @RequestParam(value = "toDate", required = false) String toDateStr) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
        Date fromDate = null;
        Date toDate = null;
        try {
            if (fromDateStr != null) {
                fromDate = dateFormat.parse(fromDateStr);
            }
            if (toDateStr != null) {
                toDate = dateFormat.parse(toDateStr);
            }
            List<MasterDTO> topMasters = workService.getTopMasters(fromDate, toDate);
            return ResponseEntity.ok(topMasters);
        } catch (ParseException e) {
            return ResponseEntity.badRequest().body("Неверный формат даты");
        } catch (java.text.ParseException e) {
            throw new RuntimeException(e);
        }
    }

}
