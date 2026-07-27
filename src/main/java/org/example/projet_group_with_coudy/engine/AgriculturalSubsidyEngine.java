package org.example.projet_group_with_coudy.engine;

import org.example.projet_group_with_coudy.model.AllocationStatus;
import org.example.projet_group_with_coudy.model.CropType;
import org.example.projet_group_with_coudy.model.FarmDeclaration;
import org.example.projet_group_with_coudy.model.SubsidyAllocation;
import org.example.projet_group_with_coudy.port.MeteorologyPort;
import org.example.projet_group_with_coudy.port.PhytosanitaryInspectionPort;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.EnumMap;
import java.util.Map;
import java.util.Set;

public class AgriculturalSubsidyEngine {

    private static final int SCALE = 2;
    private static final BigDecimal RATE_EXPORT_PER_HECTARE = new BigDecimal("50000");
    private static final BigDecimal RATE_VIVRIERE_PER_HECTARE = new BigDecimal("100000");
    private static final Set<CropType> VIVRIERE_CROPS = Set.of(CropType.MIL, CropType.SORGHO, CropType.MAIS);
    private static final BigDecimal TAUX_BONUS_ECOLOGIQUE = new BigDecimal("0.15");
    private static final BigDecimal TAUX_PENALITE_SOUS_PRODUCTION = new BigDecimal("0.50");
    private static final BigDecimal FONDS_URGENCE_FORFAITAIRE = new BigDecimal("500000");
    private static final BigDecimal SEUIL_AUDIT_PHYTOSANITAIRE = new BigDecimal("10000000.00");

    private static final Map<CropType, BigDecimal> SEUILS_CRITIQUES_KG_PAR_HECTARE = new EnumMap<>(CropType.class);

    static {
        SEUILS_CRITIQUES_KG_PAR_HECTARE.put(CropType.MIL, new BigDecimal("500"));
        SEUILS_CRITIQUES_KG_PAR_HECTARE.put(CropType.SORGHO, new BigDecimal("600"));
        SEUILS_CRITIQUES_KG_PAR_HECTARE.put(CropType.MAIS, new BigDecimal("1500"));
        SEUILS_CRITIQUES_KG_PAR_HECTARE.put(CropType.ARACHIDE, new BigDecimal("800"));
        SEUILS_CRITIQUES_KG_PAR_HECTARE.put(CropType.COTON, new BigDecimal("1000"));
    }

    private final MeteorologyPort meteorologyPort;
    private final PhytosanitaryInspectionPort phytosanitaryInspectionPort;

    public AgriculturalSubsidyEngine(
            MeteorologyPort meteorologyPort, PhytosanitaryInspectionPort phytosanitaryInspectionPort) {
        this.meteorologyPort = meteorologyPort;
        this.phytosanitaryInspectionPort = phytosanitaryInspectionPort;
    }

    public SubsidyAllocation calculate(FarmDeclaration declaration) {
        BigDecimal baseSubsidy = calculateBaseSubsidy(declaration);
        BigDecimal ecologicalBonus = calculateEcologicalBonus(declaration, baseSubsidy);
        BigDecimal subsidyBeforePenalty = baseSubsidy.add(ecologicalBonus);

        boolean secheresseSevere = meteorologyPort.isSevereDrought(declaration.location());
        boolean rendementInsuffisant = declaration.declaredYieldPerHectare()
                .compareTo(SEUILS_CRITIQUES_KG_PAR_HECTARE.get(declaration.cropType())) < 0;

        BigDecimal underproductionPenalty = (rendementInsuffisant && !secheresseSevere)
                ? subsidyBeforePenalty.multiply(TAUX_PENALITE_SOUS_PRODUCTION).setScale(SCALE, RoundingMode.HALF_UP)
                : BigDecimal.ZERO.setScale(SCALE);

        BigDecimal emergencyFund = secheresseSevere
                ? FONDS_URGENCE_FORFAITAIRE.setScale(SCALE, RoundingMode.HALF_UP)
                : BigDecimal.ZERO.setScale(SCALE);

        BigDecimal finalAmount = subsidyBeforePenalty.subtract(underproductionPenalty).add(emergencyFund);

        boolean auditRequis = finalAmount.compareTo(SEUIL_AUDIT_PHYTOSANITAIRE) > 0;
        if (auditRequis) {
            phytosanitaryInspectionPort.reportForInspection(declaration.farmId(), finalAmount);
        }

        return new SubsidyAllocation(
                declaration.farmId(),
                baseSubsidy,
                ecologicalBonus,
                underproductionPenalty,
                emergencyFund,
                finalAmount,
                auditRequis ? AllocationStatus.EN_ATTENTE_AUDIT : AllocationStatus.ALLOUE);
    }

    private BigDecimal calculateBaseSubsidy(FarmDeclaration declaration) {
        BigDecimal ratePerHectare = VIVRIERE_CROPS.contains(declaration.cropType())
                ? RATE_VIVRIERE_PER_HECTARE
                : RATE_EXPORT_PER_HECTARE;
        return declaration.hectares().multiply(ratePerHectare).setScale(SCALE, RoundingMode.HALF_UP);
    }

    private BigDecimal calculateEcologicalBonus(FarmDeclaration declaration, BigDecimal baseSubsidy) {
        if (!declaration.organicCertified()) {
            return BigDecimal.ZERO.setScale(SCALE);
        }
        return baseSubsidy.multiply(TAUX_BONUS_ECOLOGIQUE).setScale(SCALE, RoundingMode.HALF_UP);
    }
}
