package com.safroalex.AutoServiceERP.service;

import com.safroalex.AutoServiceERP.model.Master;
import com.safroalex.AutoServiceERP.repository.MasterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MasterService {
    private final MasterRepository masterRepository;

    @Autowired
    public MasterService(MasterRepository masterRepository) {
        this.masterRepository = masterRepository;
    }

    public Master saveMaster(Master master) {
        return masterRepository.save(master);
    }

    public List<Master> getAllMasters() {
        return masterRepository.findAll();
    }

    // Методы для обновления и удаления
}