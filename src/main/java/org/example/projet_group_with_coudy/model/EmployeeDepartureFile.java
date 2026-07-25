package org.example.projet_group_with_coudy.model;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Dossier de depart d'un employe, tel que transmis par le service RH.
 * Record immuable : garantit qu'un dossier ne peut pas etre modifie
 * une fois recu par le moteur de calcul.
 */
public record EmployeeDepartureFile(
        String employeeId,
        LocalDate hireDate,
        LocalDate departureDate,
        DepartureReason reason,
        BigDecimal baseMonthlySalary,
        int remainingLeaveDays,
        boolean noticePeriodRespected
) {
}
