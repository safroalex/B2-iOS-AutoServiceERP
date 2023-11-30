package com.safroalex.AutoServiceERP.service;

import com.safroalex.AutoServiceERP.exception.ResourceNotFoundException;
import com.safroalex.AutoServiceERP.model.Work;
import com.safroalex.AutoServiceERP.repository.WorkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkService {

    private final WorkRepository workRepository;

    @Autowired
    public WorkService(WorkRepository workRepository) {
        this.workRepository = workRepository;
    }

    // Создать новую работу
    public Work createWork(Work work) {
        return workRepository.save(work);
    }

    // Получить список всех работ
    public List<Work> getAllWorks() {
        return workRepository.findAll();
    }

    // Получить работу по ID
    public Work getWorkById(Long id) {
        return workRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Work not found with id : " + id));
    }

    // Обновить информацию о работе
    public Work updateWork(Long id, Work workDetails) {
        Work work = workRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Work not found with id : " + id));
        work.setDateWork(workDetails.getDateWork());
        work.setMaster(workDetails.getMaster());
        work.setCar(workDetails.getCar());
        work.setService(workDetails.getService());
        return workRepository.save(work);
    }

    // Удалить работу
    public void deleteWork(Long id) {
        Work work = workRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Work not found with id : " + id));
        workRepository.delete(work);
    }
}
