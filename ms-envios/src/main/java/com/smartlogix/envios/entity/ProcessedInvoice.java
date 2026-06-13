package com.smartlogix.envios.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "processed_invoices")
public class ProcessedInvoice {

    @Id
    private String id;

    private LocalDateTime processedAt;

    public ProcessedInvoice() {
    }

    public ProcessedInvoice(String id, LocalDateTime processedAt) {
        this.id = id;
        this.processedAt = processedAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDateTime getProcessedAt() {
        return processedAt;
    }

    public void setProcessedAt(LocalDateTime processedAt) {
        this.processedAt = processedAt;
    }
}
