package org.example.projet_group_with_coudy.engine;

import org.example.projet_group_with_coudy.model.AllocationStatus;
import org.example.projet_group_with_coudy.model.CropType;
import org.example.projet_group_with_coudy.model.FarmDeclaration;
import org.example.projet_group_with_coudy.model.SubsidyAllocation;
import org.example.projet_group_with_coudy.port.MeteorologyPort;
import org.example.projet_group_with_coudy.port.PhytosanitaryInspectionPort;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Set;

public class AgriculturalSubsidyEngine {

    private static final int SCALE = 2;
    private static final BigDecimal RATE_EXPORT_PER_HECTARE = new BigDecimal("50000");
    private static final BigDecimal RATE_VIVRIERE_PER_HECTARE = new BigDecimal("100000");
    private static final Set<CropType> VIVRIERE_CROPS = Set.of(CropType.MIL, CropType.SORGHO, CropType.MAIS);

    private final MeteorologyPort meteorologyPort;
    private final PhytosanitaryInspectionPort phytosanitaryInspectionPort;

    public AgriculturalSubsidyEngine(
            MeteorologyPort meteorologyPort, PhytosanitaryInspectionPort phytosanitaryInspectionPort) {
        this.meteorologyPort = meteorologyPort;
        this.phytosanitaryInspectionPort = phytosanitaryInspectionPort;
    }

    public SubsidyAllocation calculate(FarmDeclaration declaration) {
        BigDecimal baseSubsidy = calculateBaseSubsidy(declaration);
        BigDecimal ecologicalBonus = BigDecimal.ZERO.setScale(SCALE);
        BigDecimal underproductionPenalty = BigDecimal.ZERO.setScale(SCALE);
        BigDecimal emergencyFund = BigDecimal.ZERO.setScale(SCALE);

        BigDecimal finalAmount = baseSubsidy.add(ecologicalBonus).subtract(underproductionPenalty).add(emergencyFund);

        return new SubsidyAllocation(
                declaration.farmId(),
                baseSubsidy,
                ecologicalBonus,
                underproductionPenalty,
                emergencyFund,
                finalAmount,
                AllocationStatus.ALLOUE);
    }

    private BigDecimal calculateBaseSubsidy(FarmDeclaration declaration) {
        BigDecimal ratePerHectare = VIVRIERE_CROPS.contains(declaration.cropType())
                ? RATE_VIVRIERE_PER_HECTARE
                : RATE_EXPORT_PER_HECTARE;
        return declaration.hectares().multiply(ratePerHectare).setScale(SCALE, RoundingMode.HALF_UP);
    }
}
