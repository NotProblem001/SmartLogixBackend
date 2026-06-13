package com.smartlogix.envios.client;

import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;

@Component
public class MockBankGatewayClient implements BankGatewayClient {

    @Override
    public Map<String, Object> processDebit(String accountId, double amount) {
        Map<String, Object> response = new HashMap<>();
        if ("INSUFFICIENT_FUNDS".equalsIgnoreCase(accountId)) {
            response.put("status", "DECLINED");
            response.put("reason", "Fondos insuficientes");
        } else if ("TIMEOUT".equalsIgnoreCase(accountId)) {
            throw new RuntimeException("Timeout de conexión con el banco");
        } else {
            response.put("status", "SUCCESS");
        }
        return response;
    }
}
