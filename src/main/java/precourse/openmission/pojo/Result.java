package precourse.openmission.pojo;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 각 로또의 해당하는 Rank들을 관리하는 일급 컬렉션입니다.
 */
public class Result {
    private static final int AMOUNT_PER_LOTTO = 1000;
    private static final double PERCENTAGE_RATE = 100;
    private List<Rank> lottoResults;

    public Result(List<Rank> lottoResults) {
        this.lottoResults = lottoResults;
    }

    /**
     * 최종 수익률을 계산합니다.
     *
     * @return 둘째자리에서의 반올림한 수익률
     */
    public double profitRate() {
        double originRate = calculateRate();
        return Math.round(originRate * PERCENTAGE_RATE) / PERCENTAGE_RATE;
    }

    private long totalProfit() {
        return lottoResults.stream()
                .mapToLong(result -> result.getPrize())
                .sum();
    }

    private double calculateRate() {
        return totalProfit() * PERCENTAGE_RATE / (lottoResults.size() * AMOUNT_PER_LOTTO);
    }

    /**
     * 등수별 상금과 각 로또가 등수의 당첨된 횟수를 모아 ResultDTO의 리스트로 반환합니다.
     * Map을 이용하여, ResultDTO 형식으로 가공합니다.
     *
     * @return 계산이 완료된 ResultDTO의 리스트
     */
    public List<ResultDTO> getResults() {
        Map<Integer, Integer> rankCounts = resultsToMap();

        List<ResultDTO> results = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : rankCounts.entrySet()) {
            results.add(new ResultDTO(entry.getKey(), entry.getValue()));
        }
        return results;
    }

    /**
     * 당첨 결과를 Map에 반영합니다.
     *
     * @return 당첨 결과가 반영된 Map
     */
    private Map<Integer, Integer> resultsToMap() {
        Map<Integer, Integer> rankCounts = initializeMap();
        for (Rank r : lottoResults) {
            if (r == Rank.Other) {
                continue;
            }
            int prize = r.getPrize();
            rankCounts.put(prize, rankCounts.get(prize) + 1);
        }
        return rankCounts;
    }

    /**
     * 각 등수의 당첨된 횟수를 0회로 초기화합니다.
     * 당첨되지 않은 경우 0회를 출력해야 되기 때문에 이 작업을 별도의 도우미 함수로 분리했습니다.
     *
     * @return 당첨 횟수가 0회로 초기화된 Map
     */
    private Map<Integer, Integer> initializeMap() {
        Map<Integer, Integer> rankCounts = new HashMap<>();
        for (Rank r : Rank.values()) {
            if (r == Rank.Other) {
                continue;
            }
            rankCounts.put(r.getPrize(), 0);
        }
        return rankCounts;
    }

}
