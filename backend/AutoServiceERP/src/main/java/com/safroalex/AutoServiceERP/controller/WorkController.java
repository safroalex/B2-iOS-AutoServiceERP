package com.safroalex.AutoServiceERP.controller;

import com.safroalex.AutoServiceERP.model.Work;
import com.safroalex.AutoServiceERP.service.WorkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/works")
public class WorkController {

    private final WorkService workService;

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
}
