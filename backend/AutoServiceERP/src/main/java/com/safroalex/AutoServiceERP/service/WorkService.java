package com.safroalex.AutoServiceERP.service;

import com.safroalex.AutoServiceERP.dto.MasterDTO;
import com.safroalex.AutoServiceERP.exception.ResourceNotFoundException;
import com.safroalex.AutoServiceERP.model.Work;
import com.safroalex.AutoServiceERP.repository.WorkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Date;
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

    /**
     * Получить общую стоимость работ за указанный период времени для отечественных или импортных автомобилей.
     *
     * @param fromDate Начальная дата периода
     * @param toDate Конечная дата периода
     * @return Общая стоимость работ
     */
    public Double getTotalCost(Date fromDate, Date toDate) {
        return workRepository.sumTotalCost(fromDate, toDate);
    }

    public List<MasterDTO> getTopMasters(Date fromDate, Date toDate) {
        Pageable topFive = PageRequest.of(0, 5); // Получаем первые 5 результатов
        return workRepository.listTopMasters(fromDate, toDate, topFive);
    }
}
