package org.example.projet_group_with_coudy.mapper;

import org.example.projet_group_with_coudy.dto.DossierDepart;
import org.example.projet_group_with_coudy.dto.MotifDepart;
import org.example.projet_group_with_coudy.dto.SoldeToutCompte;
import org.example.projet_group_with_coudy.model.DepartureReason;
import org.example.projet_group_with_coudy.model.EmployeeDepartureFile;
import org.example.projet_group_with_coudy.model.SeveranceStatement;
import org.springframework.stereotype.Component;

@Component
public class SoldeToutCompteMapper {

    public EmployeeDepartureFile toDomain(DossierDepart dto) {
        return new EmployeeDepartureFile(
                dto.getEmployeeId(),
                dto.getDateDebutContrat(),
                dto.getDateFinContrat(),
                toDomainReason(dto.getMotifDepart()),
                dto.getSalaireMensuelBase(),
                dto.getJoursCongesRestants(),
                dto.getPreavisRespecte());
    }

    public SoldeToutCompte toDto(SeveranceStatement statement) {
        return new SoldeToutCompte(
                statement.employeeId(),
                statement.paidLeaveIndemnity(),
                statement.seniorityBonus(),
                statement.noticeViolationPenalty(),
                statement.grossAmount(),
                statement.taxWithholding(),
                statement.netAmount(),
                statement.auditFlagged());
    }

    private DepartureReason toDomainReason(MotifDepart motifDepart) {
        return switch (motifDepart) {
            case DEMISSION -> DepartureReason.DEMISSION;
            case LICENCIEMENT_ECONOMIQUE -> DepartureReason.LICENCIEMENT_ECONOMIQUE;
            case LICENCIEMENT_FAUTE_GRAVE -> DepartureReason.LICENCIEMENT_FAUTE_GRAVE;
            case RETRAITE -> DepartureReason.RETRAITE;
        };
    }
}
