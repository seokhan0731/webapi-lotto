package precourse.openmission.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class LottosTest {
    @DisplayName("발행할 로또 수에 맞춘 로또들 생성")
    @Test
    void generateLottos() {
        Amount amount = new Amount(2000);
        Lottos lottos = new Lottos(amount.buy());
        NumberGenerator a = new FakeGenerator(List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12));
        lottos.issueLottos(a);
        assertThat(lottos.getLottos().size()).isEqualTo(2);
    }

    @DisplayName("일치하는 번호의 수 반환")
    @Test
    void numberMatched() {
        Amount amount = new Amount(2000);
        Lottos lottos = new Lottos(amount.buy());
        NumberGenerator a = new FakeGenerator(List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 1));
        lottos.issueLottos(a);
        Lotto winningLotto = new Lotto(List.of(1, 2, 3, 4, 5, 6));
        List<Integer> numbermatched = lottos.matchNumbersAll(winningLotto);
        assertThat(numbermatched).hasSize(2);
        assertThat(numbermatched).containsExactly(6, 1);
    }

    @DisplayName("보너스 번호와의 일치 여부 반환")
    @Test
    void bonusNumberMatched() {
        Amount amount = new Amount(2000);
        Lottos myLottos = new Lottos(amount.buy());
        NumberGenerator a = new FakeGenerator(List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 1));
        myLottos.issueLottos(a);
        Lotto winningLotto = new Lotto(List.of(1, 2, 3, 4, 5, 6));
        Bonus number = new Bonus(7, winningLotto);

        List<Boolean> bonusNumberMatched = myLottos.matchBonusAll(number);
        assertThat(bonusNumberMatched).hasSize(2);
        assertThat(bonusNumberMatched).containsExactly(false, true);
    }

    @DisplayName("여러개의 로또 번호 반환")
    @Test
    void sortedLottosNumbers() {
        Lottos myLottos = new Lottos(2);
        NumberGenerator a = new FakeGenerator(List.of(1, 3, 6, 8, 2, 10, 12, 5, 7, 3, 9, 25));
        myLottos.issueLottos(a);
        List<Set<Integer>> lottosNumbers = myLottos.getNumbersAll();
        assertThat(lottosNumbers).hasSize(2);
        assertThat(lottosNumbers)
                .containsExactlyInAnyOrder(Set.of(1, 2, 3, 6, 8, 10), Set.of(3, 5, 7, 9, 12, 25));
    }
}
