package precourse.openmission.result;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 최종 결과 조회를 책임지는 컨트롤러 계층입니다.
 */
@RestController
@RequiredArgsConstructor
public class ResultController {
    private final ResultService resultService;

    /**
     * 최종 결과를 조회합니다.
     * ResultService에서 계산한 후, 넘겨준 값을 DTO로 포장하여 반환합니다.
     *
     * @param id 구매 id
     * @return 상태 코드 200과 결과값(수익률, 등수 결과)
     * @throws IllegalArgumentException 유효하지 않은 id 입력 시, 발생합니다.
     * @throws IllegalStateException 최종 결과를 계산하는데 필요한, 구성 요소 중, 하나라도 존재하지 않다면 발생합니다.
     */
    @GetMapping(value = "/result/{id}")
    public ResultResponseDTO getResult(@PathVariable Long id) {
        double profitRate = resultService.getProfitRate(id);
        Map<String, Integer> finalRank = resultService.getRankResult(id);

        return new ResultResponseDTO(id, profitRate, finalRank);
    }


}
