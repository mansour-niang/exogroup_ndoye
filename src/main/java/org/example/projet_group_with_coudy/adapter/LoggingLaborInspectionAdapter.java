package org.example.projet_group_with_coudy.adapter;

import org.example.projet_group_with_coudy.port.LaborInspectionPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Implementation temporaire de {@link LaborInspectionPort}, en attendant le
 * branchement au systeme reel de l'Inspection du Travail. Se contente de
 * journaliser la notification au lieu d'appeler un systeme externe.
 */
@Component
public class LoggingLaborInspectionAdapter implements LaborInspectionPort {

    private static final Logger log = LoggerFactory.getLogger(LoggingLaborInspectionAdapter.class);

    @Override
    public void reportSeveranceForAudit(String employeeId, BigDecimal netAmount) {
        log.warn("Audit a declencher (systeme reel non branche) : employeeId={}, montantNet={}",
                employeeId, netAmount);
    }
}
