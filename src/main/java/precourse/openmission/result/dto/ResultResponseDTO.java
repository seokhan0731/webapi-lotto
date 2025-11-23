package precourse.openmission.result.dto;

import lombok.Getter;

import java.util.Map;

/**
 * 로또 최종 결과 반환시, 반환값을 정의하는 DTO
 */
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
