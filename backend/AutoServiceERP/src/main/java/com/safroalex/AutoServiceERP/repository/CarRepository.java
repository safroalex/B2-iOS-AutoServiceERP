package com.safroalex.AutoServiceERP.repository;

import com.safroalex.AutoServiceERP.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CarRepository extends JpaRepository<Car, Long> {
    List<Car> findByColor(String color);
    List<Car> findByMark(String mark);
    Optional<Car> findByNum(String num);
}
