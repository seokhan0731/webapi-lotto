package precourse.openmission.result;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class ResultController {
    private final ResultService resultService;

    @GetMapping(value = "/result/{id}")
    public ResultResponseDTO getResult(@PathVariable Long id) {
        double profitRate = resultService.getProfitRate(id);
        Map<String, Integer> finalRank = resultService.getRankResult(id);

        return new ResultResponseDTO(id, profitRate, finalRank);
    }
}
