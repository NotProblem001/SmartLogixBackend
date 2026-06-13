package com.smartlogix.envios.service;

import com.smartlogix.envios.client.BankGatewayClient;
import com.smartlogix.envios.entity.Invoice;
import com.smartlogix.envios.exception.InvalidAccountException;
import com.smartlogix.envios.exception.PaymentDeclinedException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
public class EFTPaymentService {

    private final BankGatewayClient bankGatewayClient;
    private final Set<String> processedInvoiceIds = new HashSet<>();

    public EFTPaymentService(BankGatewayClient bankGatewayClient) {
        this.bankGatewayClient = bankGatewayClient;
    }

    public void processPayment(Invoice invoice) {
        if (invoice == null) {
            throw new IllegalArgumentException("La factura no puede ser nula");
        }

        // 1. Validation check
        if (invoice.getAccountId() == null || invoice.getAccountId().trim().isEmpty()) {
            throw new InvalidAccountException("Cuenta EFT no puede ser nula o vacía");
        }

        // 2. Idempotency Check (Double debit protection)
        synchronized (processedInvoiceIds) {
            if (processedInvoiceIds.contains(invoice.getId())) {
                invoice.setStatus("PAID");
                return;
            }
        }

        // 3. Retry block for bank communication failures (up to 3 attempts)
        int maxRetries = 3;
        Map<String, Object> response = null;
        Exception lastException = null;

        for (int attempt = 1; attempt <= maxRetries; attempt++) {
            try {
                response = bankGatewayClient.processDebit(invoice.getAccountId(), invoice.getAmount());
                lastException = null; // Clear if succeeded
                break;
            } catch (Exception e) {
                lastException = e;
                // Wait slightly before retry (or just loop in unit test mock context)
            }
        }

        if (lastException != null) {
            invoice.setStatus("PAGO_FALLIDO");
            throw new PaymentDeclinedException("Error de conexión con el banco tras varios intentos: " + lastException.getMessage(), lastException);
        }

        // 4. Process Bank Response
        if (response == null || !"SUCCESS".equals(response.get("status"))) {
            invoice.setStatus("PAGO_FALLIDO");
            String reason = response != null ? (String) response.get("reason") : "Respuesta bancaria nula";
            throw new PaymentDeclinedException("Pago declinado por el banco: " + reason);
        }

        // 5. Finalize payment status
        invoice.setStatus("PAID");
        synchronized (processedInvoiceIds) {
            processedInvoiceIds.add(invoice.getId());
        }
    }
}
