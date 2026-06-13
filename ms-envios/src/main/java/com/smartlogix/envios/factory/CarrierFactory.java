package com.smartlogix.envios.factory;

import org.springframework.stereotype.Component;

/**
 * RF-E01: Patrón Factory Method.
 * Instancia dinámicamente distintos proveedores de transporte según el tipo de transportista.
 */
@Component
public class CarrierFactory {

    public ShippingProvider getCarrier(String carrierType) {
        if (carrierType == null) {
            throw new IllegalArgumentException("El tipo de transportista no puede ser nulo");
        }
        
        switch (carrierType.toUpperCase()) {
            case "DHL":
                return new DhlShippingProvider();
            case "EXPRESS":
                return new ExpressShippingProvider();
            case "LOCAL":
                return new LocalShippingProvider();
            default:
                throw new IllegalArgumentException("Proveedor de envío no soportado: " + carrierType);
        }
    }
}

// Implementación Concreta 1: DHL
class DhlShippingProvider implements ShippingProvider {
    @Override
    public void dispatch(Long orderId) {
        System.out.println("Despachando por DHL EXPRESS el pedido: " + orderId);
        // Aquí iría el cliente HTTP (RF-E02) llamando a la API de DHL
    }
}

// Implementación Concreta 2: Express
class ExpressShippingProvider implements ShippingProvider {
    @Override
    public void dispatch(Long orderId) {
        System.out.println("Despachando por Envío Express el pedido: " + orderId);
    }
}

// Implementación Concreta 3: Local
class LocalShippingProvider implements ShippingProvider {
    @Override
    public void dispatch(Long orderId) {
        System.out.println("Despachando por Correo Local Estándar el pedido: " + orderId);
    }
}
