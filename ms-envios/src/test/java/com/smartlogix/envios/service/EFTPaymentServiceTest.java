package com.smartlogix.envios.service;

import com.smartlogix.envios.client.BankGatewayClient;
import com.smartlogix.envios.entity.Invoice;
import com.smartlogix.envios.exception.InvalidAccountException;
import com.smartlogix.envios.exception.PaymentDeclinedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class EFTPaymentServiceTest {

    @Mock
    private BankGatewayClient bankGatewayClient;

    @InjectMocks
    private EFTPaymentService eftPaymentService;

    private Invoice invoice;

    @BeforeEach
    public void setUp() {
        invoice = new Invoice("INV-001", 500.0, "PENDING", "ACCT-12345");
    }

    @Test
    public void testEFTDebit_SuccessfulExecution_UpdatesLedger() {
        Mockito.when(bankGatewayClient.processDebit("ACCT-12345", 500.0))
                .thenReturn(Map.of("status", "SUCCESS", "receipt", "REC-999"));

        eftPaymentService.processPayment(invoice);

        assertEquals("PAID", invoice.getStatus());
        verify(bankGatewayClient, times(1)).processDebit("ACCT-12345", 500.0);
    }

    @Test
    public void testEFTDebit_InsufficientFunds_TriggersExceptionAndAlert() {
        Mockito.when(bankGatewayClient.processDebit("ACCT-12345", 500.0))
                .thenReturn(Map.of("status", "DECLINED", "reason", "INSUFFICIENT_FUNDS"));

        PaymentDeclinedException exception = assertThrows(PaymentDeclinedException.class, () -> {
            eftPaymentService.processPayment(invoice);
        });

        assertTrue(exception.getMessage().contains("Pago declinado por el banco: INSUFFICIENT_FUNDS"));
        assertEquals("PAGO_FALLIDO", invoice.getStatus());
    }

    @Test
    public void testIdempotency_DoubleDebit() {
        Mockito.when(bankGatewayClient.processDebit("ACCT-12345", 500.0))
                .thenReturn(Map.of("status", "SUCCESS"));

        // Call 1
        eftPaymentService.processPayment(invoice);
        assertEquals("PAID", invoice.getStatus());

        // Call 2
        eftPaymentService.processPayment(invoice);
        assertEquals("PAID", invoice.getStatus());

        // Verify bank client was called ONLY once (idempotent)
        verify(bankGatewayClient, times(1)).processDebit("ACCT-12345", 500.0);
    }

    @Test
    public void testValidation_InvalidAccount() {
        // Null account ID
        invoice.setAccountId(null);
        assertThrows(InvalidAccountException.class, () -> {
            eftPaymentService.processPayment(invoice);
        });

        // Empty account ID
        invoice.setAccountId("   ");
        assertThrows(InvalidAccountException.class, () -> {
            eftPaymentService.processPayment(invoice);
        });
    }

    @Test
    public void testRetry_BankTimeout() {
        Mockito.when(bankGatewayClient.processDebit(anyString(), anyDouble()))
                .thenThrow(new RuntimeException("SocketTimeoutException: Connection timed out"));

        PaymentDeclinedException exception = assertThrows(PaymentDeclinedException.class, () -> {
            eftPaymentService.processPayment(invoice);
        });

        assertTrue(exception.getMessage().contains("Error de conexión con el banco tras varios intentos"));
        assertEquals("PAGO_FALLIDO", invoice.getStatus());

        // Verify processDebit was retried exactly 3 times
        verify(bankGatewayClient, times(3)).processDebit("ACCT-12345", 500.0);
    }
}
