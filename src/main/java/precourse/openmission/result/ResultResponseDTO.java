package precourse.openmission.result;

import lombok.Getter;

import java.util.Map;

@Getter
public class ResultResponseDTO {
    private Long purchaseId;
    private Map<String, Integer> rankCounts;
    private double profitRate;

    public ResultResponseDTO(Long purchaseId, double profitRate, Map<String, Integer> results) {
        this.purchaseId = purchaseId;
        this.rankCounts = results;
        this.profitRate = profitRate;
    }
}
