package com.smartlogix.envios.service;

import com.smartlogix.envios.entity.Shipment;
import com.smartlogix.envios.factory.CarrierFactory;
import com.smartlogix.envios.factory.ShippingProvider;
import com.smartlogix.envios.repository.ShipmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ShipmentService {

    private final ShipmentRepository repository;
    private final CarrierFactory carrierFactory;

    public ShipmentService(ShipmentRepository repository, CarrierFactory carrierFactory) {
        this.repository = repository;
        this.carrierFactory = carrierFactory;
    }

    public Iterable<Shipment> getAllShipments() {
        return repository.findAll();
    }

    @Transactional
    public Shipment createShipment(Shipment shipment) {
        if (shipment.getStatus() == null) {
            shipment.setStatus("PENDIENTE");
        }
        
        // RF-E01: Patrón Factory Method para instanciar dinámicamente y despachar según el JSON de entrada
        if (shipment.getCarrier() != null) {
            try {
                ShippingProvider provider = carrierFactory.getCarrier(shipment.getCarrier());
                provider.dispatch(shipment.getOrderId());
                shipment.setStatus("DESPACHADO");
            } catch (Exception e) {
                System.err.println("Error despachando envío: " + e.getMessage());
                shipment.setStatus("ERROR");
            }
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
