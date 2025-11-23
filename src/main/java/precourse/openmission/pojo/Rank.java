package precourse.openmission.pojo;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 로또 당첨 등수를 정의한 열거형입니다.
 * 각 등수는 6개의 일치 개수와 상금에 대한 정보를 추가로 가집니다.
 */
public enum Rank {
    First(6, 2000000000),
    Second(5, 30000000),
    Third(5, 1500000),
    Fourth(4, 50000),
    Fifth(3, 5000),
    Other(0, 0);

    private final int matchCount;
    private final int prize;

    Rank(int matchCount, int prize) {
        this.matchCount = matchCount;
        this.prize = prize;
    }

    /**
     * 발행된 로또들의 당첨 등수를 반환합니다.
     *
     * @param matchCounts  로또들의 당첨 번호 일치 개수 리스트
     * @param matchBonuses 로또들의 보너스 번호 일치 여부 리스트
     * @return 로또들의 당첨 등수 Rank 객체 리스트
     */
    public static List<Rank> valueOf(List<Integer> matchCounts, List<Boolean> matchBonuses) {
        List<Rank> result = new ArrayList<>();
        for (int i = 0; i < matchCounts.size(); i++) {
            result.add(decideRank(matchCounts.get(i), matchBonuses.get(i)));
        }
        return result;
    }

    private static Rank decideRank(Integer matchCount, Boolean matchBonus) {
        if (matchCount == 6) {
            return First;
        }
        if (matchCount == 5) {
            return decideByBonus(matchBonus);
        }
        if (matchCount == 4) {
            return Fourth;
        }
        if (matchCount == 3) {
            return Fifth;
        }
        return Other;
    }

    private static Rank decideByBonus(Boolean matchBonus) {
        if (matchBonus) {
            return Second;
        }
        return Third;
    }

    /**
     * 각 등수별 일치 개수와 상금 정보를 DTO로 묶어 반환합니다.
     * Other을 제외한 출력에 필요한 1~5등의 선별 기준만을 반환합니다.
     *
     * @return 1~5등까지의 정보가 담긴 CategoryDTO 리스트
     */
    public static List<CategoryDTO> getCategory() {
        List<Rank> rank = Arrays.asList(Rank.values());
        List<CategoryDTO> category = new ArrayList<>();
        for (Rank r : rank) {
            if (r == Other) {
                continue;
            }
            category.add(new CategoryDTO(r.matchCount, r.prize));
        }
        return category;
    }

    public int getPrize() {
        return prize;
    }
}
