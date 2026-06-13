package com.smartlogix.envios.controller;

import com.smartlogix.envios.dto.Load;
import com.smartlogix.envios.entity.Driver;
import com.smartlogix.envios.repository.DriverRepository;
import com.smartlogix.envios.service.MatchingEngineService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/matching")
public class MatchingController {

    private final MatchingEngineService matchingEngineService;
    private final DriverRepository driverRepository;

    public MatchingController(MatchingEngineService matchingEngineService, DriverRepository driverRepository) {
        this.matchingEngineService = matchingEngineService;
        this.driverRepository = driverRepository;
    }

    @PostMapping("/find-driver")
    public ResponseEntity<Driver> findDriverForLoad(@RequestBody Load load) {
        List<Driver> availableDrivers = driverRepository.findByAvailableTrue();
        Driver bestDriver = matchingEngineService.findBestDriver(load, availableDrivers);
        if (bestDriver == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(bestDriver);
    }
}
