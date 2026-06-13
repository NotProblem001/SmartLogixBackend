package com.smartlogix.envios.service;

import com.smartlogix.envios.dto.Load;
import com.smartlogix.envios.entity.Driver;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MatchingEngineService {

    private static final double EARTH_RADIUS_KM = 6371.0;
    private static final double MAX_DISTANCE_KM = 50.0;

    public Driver findBestDriver(Load load, List<Driver> drivers) {
        if (load == null || drivers == null) {
            return null;
        }

        List<Driver> candidates = drivers.stream()
                .filter(Driver::isAvailable)
                // 1. Hard constraint: Vehicle type match
                .filter(d -> d.getVehicleType() != null && d.getVehicleType().equalsIgnoreCase(load.getRequiredVehicleType()))
                // 2. Hard constraint: Capacity >= Load weight
                .filter(d -> d.getMaxCapacity() >= load.getWeight())
                // 3. Hard constraint: Proximity <= 50 km (Haversine formula)
                .filter(d -> calculateDistance(load.getOriginLatitude(), load.getOriginLongitude(), d.getLatitude(), d.getLongitude()) <= MAX_DISTANCE_KM)
                // 4. Soft constraint: Sort by recentness (newest update first)
                .sorted((d1, d2) -> {
                    if (d1.getLastStatusUpdate() == null && d2.getLastStatusUpdate() == null) return 0;
                    if (d1.getLastStatusUpdate() == null) return 1;
                    if (d2.getLastStatusUpdate() == null) return -1;
                    return d2.getLastStatusUpdate().compareTo(d1.getLastStatusUpdate());
                })
                .collect(Collectors.toList());

        return candidates.isEmpty() ? null : candidates.get(0);
    }

    public double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS_KM * c;
    }
}
