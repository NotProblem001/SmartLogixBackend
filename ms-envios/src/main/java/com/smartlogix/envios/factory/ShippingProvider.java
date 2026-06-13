package com.smartlogix.envios.factory;

/**
 * Interfaz base para los proveedores de envío.
 * Define la acción de despacho de un pedido.
 */
public interface ShippingProvider {
    void dispatch(Long orderId);
}
