package com.safroalex.AutoServiceERP.controller;

import com.safroalex.AutoServiceERP.model.Car;
import com.safroalex.AutoServiceERP.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cars")
public class CarController {
    private final CarService carService;

    @Autowired
    public CarController(CarService carService) {
        this.carService = carService;
    }

    @PostMapping
    public ResponseEntity<Car> addCar(@RequestBody Car car) {
        Car savedCar = carService.saveCar(car);
        return ResponseEntity.ok(savedCar);
    }

    @GetMapping
    public List<Car> getAllCars() {
        return carService.getAllCars();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Car> updateCar(@PathVariable(value = "id") Long carId,
                                         @RequestBody Car carDetails) {
        Car updatedCar = carService.updateCar(carId, carDetails);
        return ResponseEntity.ok(updatedCar);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCar(@PathVariable(value = "id") Long carId) {
        carService.deleteCar(carId);
        return ResponseEntity.ok().build();
    }
}
