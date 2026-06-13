package com.smartlogix.envios.client;

import java.util.Map;

public interface BankGatewayClient {
    Map<String, Object> processDebit(String accountId, double amount);
}
