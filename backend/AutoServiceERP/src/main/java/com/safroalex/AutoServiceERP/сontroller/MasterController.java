package com.safroalex.AutoServiceERP.сontroller;

import com.safroalex.AutoServiceERP.model.Master;
import com.safroalex.AutoServiceERP.service.MasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/masters")
public class MasterController {
    private final MasterService masterService;

    @Autowired
    public MasterController(MasterService masterService) {
        this.masterService = masterService;
    }

    @PostMapping
    public ResponseEntity<Master> addMaster(@RequestBody Master master) {
        Master savedMaster = masterService.saveMaster(master);
        return ResponseEntity.ok(savedMaster);
    }

    @GetMapping
    public List<Master> getAllMasters() {
        return masterService.getAllMasters();
    }

    // Методы для обновления и удаления
}

