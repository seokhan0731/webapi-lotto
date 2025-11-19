package precourse.openmission.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class RankTest {
    @DisplayName("단일 로또 등수 계산")
    @Test
    void rankOneLotto() {
        Lottos myLottos = new Lottos(1);
        NumberGenerator a = new FakeGenerator(List.of(1, 2, 3, 4, 5, 6));
        myLottos.issueLottos(a);
        Lotto winningLotto = new Lotto(List.of(1, 2, 3, 4, 5, 6));
        Bonus bonusNumber = new Bonus(30, winningLotto);
        List<Rank> result = Rank.valueOf(myLottos.matchNumbersAll(winningLotto), myLottos.matchBonusAll(bonusNumber));
        assertThat(result).hasSize(1);
        assertThat(result).containsExactly(Rank.First);
    }

    @DisplayName("1,4,5등 당첨 계산")
    @Test
    void resultExceptMatch5Numbers() {
        Lottos myLottos = new Lottos(3);
        NumberGenerator a = new FakeGenerator(List.of(1, 2, 3, 4, 5, 6, 1, 2, 3, 4, 11, 12, 1, 2, 3, 10, 11, 12));
        myLottos.issueLottos(a);
        Lotto winningLotto = new Lotto(List.of(1, 2, 3, 4, 5, 6));
        Bonus bonusNumber = new Bonus(30, winningLotto);
        List<Rank> result = Rank.valueOf(myLottos.matchNumbersAll(winningLotto), myLottos.matchBonusAll(bonusNumber));
        assertThat(result).hasSize(3);
        assertThat(result).containsExactly(Rank.First, Rank.Fourth, Rank.Fifth);
    }

    @DisplayName("2등 당첨 계산")
    @Test
    void secondRank() {
        Lottos myLottos = new Lottos(1);
        NumberGenerator a = new FakeGenerator(List.of(1, 2, 3, 4, 5, 6));
        myLottos.issueLottos(a);
        Lotto winningLotto = new Lotto(List.of(1, 2, 3, 4, 5, 7));
        Bonus bonusNumber = new Bonus(6, winningLotto);
        List<Rank> result = Rank.valueOf(myLottos.matchNumbersAll(winningLotto), myLottos.matchBonusAll(bonusNumber));
        assertThat(result).hasSize(1);
        assertThat(result).containsExactly(Rank.Second);
    }

    @DisplayName("enum 필드 값 출력")
    @Test
    void printEnumMember() {
        List<CategoryDTO> category = Rank.getCategory();
        assertThat(category).hasSize(5);
        assertThat(category).containsExactlyInAnyOrder(new CategoryDTO(3, 5000),
                new CategoryDTO(4, 50000),
                new CategoryDTO(5, 1500000),
                new CategoryDTO(5, 30000000),
                new CategoryDTO(6, 2000000000));
    }
}