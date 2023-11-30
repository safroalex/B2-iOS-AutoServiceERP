package com.safroalex.AutoServiceERP.repository;

import com.safroalex.AutoServiceERP.model.Master;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MasterRepository extends JpaRepository<Master, Long> {
    Optional<Master> findByName(String name);
}