package com.smartlogix.envios.repository;

import com.smartlogix.envios.entity.ProcessedInvoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProcessedInvoiceRepository extends JpaRepository<ProcessedInvoice, String> {
}
