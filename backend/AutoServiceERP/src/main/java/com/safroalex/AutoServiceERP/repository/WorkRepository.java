package com.safroalex.AutoServiceERP.repository;

import com.safroalex.AutoServiceERP.dto.MasterDTO;
import org.springframework.data.jpa.repository.Query;
import com.safroalex.AutoServiceERP.model.Work;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Date;
import java.util.List;


public interface WorkRepository extends JpaRepository<Work, Long> {
    List<Work> findAllByDateWorkBetween(Date startDate, Date endDate);

    @Query("SELECT SUM(CASE WHEN w.car.isForeign = true THEN s.costForeign ELSE s.costOur END) " +
            "FROM Work w JOIN w.service s WHERE w.dateWork BETWEEN :fromDate AND :toDate")
    Double sumTotalCost(Date fromDate, Date toDate);


    @Query("SELECT new com.safroalex.AutoServiceERP.dto.MasterDTO(m.id, m.name, COUNT(w)) " +
            "FROM Work w JOIN w.master m " +
            "WHERE w.dateWork BETWEEN :fromDate AND :toDate " +
            "GROUP BY m.id, m.name " +
            "ORDER BY COUNT(w) DESC")
    List<MasterDTO> listTopMasters(Date fromDate, Date toDate, Pageable pageable);

}
