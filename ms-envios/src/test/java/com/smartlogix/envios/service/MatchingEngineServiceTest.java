package com.smartlogix.envios.service;

import com.smartlogix.envios.dto.Load;
import com.smartlogix.envios.entity.Driver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MatchingEngineServiceTest {

    private MatchingEngineService matchingEngineService;
    private Load load;

    @BeforeEach
    public void setUp() {
        matchingEngineService = new MatchingEngineService();
        // Load requires flatbed, 2000kg weight, starting at Santiago coords
        load = new Load("LOAD-01", "flatbed", 2000.0, -33.456, -70.648);
    }

    @Test
    public void testStrictMatch_SuccessfulAssignment() {
        // Driver A: Flatbed, capacity 5000kg, 5km away, available
        Driver driverA = new Driver("DRV-A", "Driver A", "flatbed", 5000.0, -33.42, -70.61, LocalDateTime.now(), true);
        // Driver B: Box truck (wrong vehicle type)
        Driver driverB = new Driver("DRV-B", "Driver B", "boxtruck", 5000.0, -33.42, -70.61, LocalDateTime.now(), true);
        // Driver C: Flatbed, capacity 1000kg (insufficient capacity)
        Driver driverC = new Driver("DRV-C", "Driver C", "flatbed", 1000.0, -33.42, -70.61, LocalDateTime.now(), true);
        // Driver D: Flatbed, 5000kg capacity, but not available
        Driver driverD = new Driver("DRV-D", "Driver D", "flatbed", 5000.0, -33.42, -70.61, LocalDateTime.now(), false);

        List<Driver> drivers = Arrays.asList(driverA, driverB, driverC, driverD);
        Driver matched = matchingEngineService.findBestDriver(load, drivers);

        assertNotNull(matched);
        assertEquals("DRV-A", matched.getId());
    }

    @Test
    public void testHardConstraint_CapacityExceeded_ReturnsNull() {
        // Load is 10,000kg
        load.setWeight(10000.0);

        // Driver A only has 1500kg capacity
        Driver driverA = new Driver("DRV-A", "Driver A", "flatbed", 1500.0, -33.42, -70.61, LocalDateTime.now(), true);

        List<Driver> drivers = Arrays.asList(driverA);
        Driver matched = matchingEngineService.findBestDriver(load, drivers);

        assertNull(matched);
    }

    @Test
    public void testHaversineProximity_ExceedsThreshold_FilteredOut() {
        // Driver A: Flatbed, capacity 5000kg, but situated ~150km away in Mendoza coords
        // (Santiago: -33.456, -70.648 vs Mendoza: -32.889, -68.845 is ~180 km)
        Driver driverA = new Driver("DRV-A", "Driver A", "flatbed", 5000.0, -32.889, -68.845, LocalDateTime.now(), true);

        List<Driver> drivers = Arrays.asList(driverA);
        Driver matched = matchingEngineService.findBestDriver(load, drivers);

        assertNull(matched);
    }

    @Test
    public void testSoftConstraint_SortingByRecentness() {
        LocalDateTime baseTime = LocalDateTime.now();
        // Driver A: available, matches criteria, updated 2 hours ago
        Driver driverA = new Driver("DRV-A", "Driver A", "flatbed", 5000.0, -33.42, -70.61, baseTime.minusHours(2), true);
        // Driver B: available, matches criteria, updated 5 minutes ago
        Driver driverB = new Driver("DRV-B", "Driver B", "flatbed", 5000.0, -33.42, -70.61, baseTime.minusMinutes(5), true);
        // Driver C: available, matches criteria, updated 1 day ago
        Driver driverC = new Driver("DRV-C", "Driver C", "flatbed", 5000.0, -33.42, -70.61, baseTime.minusDays(1), true);

        List<Driver> drivers = Arrays.asList(driverA, driverB, driverC);
        Driver matched = matchingEngineService.findBestDriver(load, drivers);

        assertNotNull(matched);
        assertEquals("DRV-B", matched.getId()); // Newest status update should be picked first
    }
}
