package com.safroalex.AutoServiceERP.repository;

import com.safroalex.AutoServiceERP.model.Work;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;


public interface WorkRepository extends JpaRepository<Work, Long> {
    List<Work> findAllByDateWorkBetween(Date startDate, Date endDate);
}
