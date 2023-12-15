package com.safroalex.AutoServiceERP.service;

import com.safroalex.AutoServiceERP.exception.CarAlreadyExistsException;
import com.safroalex.AutoServiceERP.exception.ResourceNotFoundException;
import com.safroalex.AutoServiceERP.model.Car;
import com.safroalex.AutoServiceERP.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CarService {
    private final CarRepository carRepository;

    @Autowired
    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    public Car saveCar(Car car) {
        // Проверяем, существует ли машина с таким же номером
        Optional<Car> existingCar = carRepository.findByNum(car.getNum());
        if (existingCar.isPresent()) {
            throw new CarAlreadyExistsException("Car with number " + car.getNum() + " already exists");
        }

        return carRepository.save(car);
    }

    public List<Car> getAllCars() {
        return carRepository.findAll();
    }

    public Car updateCar(Long id, Car carDetails) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Car not found with id : " + id));
        car.setNum(carDetails.getNum());
        car.setColor(carDetails.getColor());
        car.setMark(carDetails.getMark());
        car.setForeign(carDetails.isForeign());
        return carRepository.save(car);
    }

    public ResponseEntity<?> deleteCar(Long id) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Car not found with id : " + id));
        carRepository.delete(car);
        return ResponseEntity.ok().build();
    }
}
