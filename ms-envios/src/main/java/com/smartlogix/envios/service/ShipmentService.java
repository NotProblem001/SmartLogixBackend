package com.smartlogix.envios.service;

import com.smartlogix.envios.entity.Shipment;
import com.smartlogix.envios.repository.ShipmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ShipmentService {

    private final ShipmentRepository repository;

    public ShipmentService(ShipmentRepository repository) {
        this.repository = repository;
    }

    public Iterable<Shipment> getAllShipments() {
        return repository.findAll();
    }

    @Transactional
    public Shipment createShipment(Shipment shipment) {
        if (shipment.getStatus() == null) {
            shipment.setStatus("PENDIENTE");
        }
        return repository.save(shipment);
    }

    @Transactional
    public Shipment updateShipment(Long id, Shipment data) {
        Shipment existing = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Envío no encontrado: " + id));
        
        if (data.getStatus() != null) existing.setStatus(data.getStatus());
        if (data.getCarrier() != null) existing.setCarrier(data.getCarrier());
        
        return repository.save(existing);
    }

    @Transactional
    public void deleteShipment(Long id) {
        repository.deleteById(id);
    }
}
