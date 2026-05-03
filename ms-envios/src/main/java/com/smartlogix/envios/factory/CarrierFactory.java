package com.smartlogix.envios.factory;

import org.springframework.stereotype.Component;

/**
 * RF-E01: Patrón Factory Method.
 * Instancia dinámicamente distintos proveedores de transporte según el tipo de pedido.
 */
@Component
public class CarrierFactory {

    public CarrierService getCarrier(String orderType) {
        if ("EXPRESS".equalsIgnoreCase(orderType)) {
            return new DhlCarrierService();
        } else {
            return new LocalPostCarrierService();
        }
    }
}

// Interfaz Base
interface CarrierService {
    void dispatch(String orderId);
}

// Implementación Concreta 1
class DhlCarrierService implements CarrierService {
    @Override
    public void dispatch(String orderId) {
        System.out.println("Despachando por DHL EXPRESS el pedido: " + orderId);
        // Aquí iría el cliente HTTP (RF-E02) llamando a la API de DHL
    }
}

// Implementación Concreta 2
class LocalPostCarrierService implements CarrierService {
    @Override
    public void dispatch(String orderId) {
        System.out.println("Despachando por Correo Local Estándar el pedido: " + orderId);
    }
}
