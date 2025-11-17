package precourse.openmission.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ResultTest {
    @DisplayName("수익률 계산")
    @Test
    void calculateProfit() {
        Lottos myLottos = new Lottos(1);
        NumberGenerator a = new FakeGenerator(List.of(1, 2, 3, 4, 5, 6));
        myLottos.issueLottos(a);
        Lotto winningLotto = new Lotto(List.of(1, 2, 3, 4, 5, 6));
        Bonus bonusNumber = new Bonus(30, winningLotto);
        List<Rank> rankResult = Rank.valueOf(myLottos.matchNumbersAll(winningLotto), myLottos.matchBonusAll(bonusNumber));

        Result priceResult = new Result(rankResult);
        assertThat(priceResult.profitRate()).isEqualTo(200000000);
    }

    @DisplayName("최종 결과 반환")
    @Test
    void rankToResultDTO() {
        Lottos myLottos = new Lottos(2);
        NumberGenerator a = new FakeGenerator(List.of(1, 2, 3, 4, 5, 6, 1, 2, 3, 4, 5, 7));
        myLottos.issueLottos(a);
        Lotto winningLotto = new Lotto(List.of(1, 2, 3, 4, 5, 6));
        Bonus bonusNumber = new Bonus(30, winningLotto);
        List<Rank> rankResult = Rank.valueOf(myLottos.matchNumbersAll(winningLotto), myLottos.matchBonusAll(bonusNumber));

        Result results = new Result(rankResult);
        List<ResultDTO> r = results.getResults();

        assertThat(r).hasSize(5);
        assertThat(r).containsExactlyInAnyOrder(new ResultDTO(2000000000, 1),
                new ResultDTO(1500000, 1),
                new ResultDTO(30000000, 0),
                new ResultDTO(50000, 0),
                new ResultDTO(5000, 0));
    }
}
