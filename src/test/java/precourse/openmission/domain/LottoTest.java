package precourse.openmission.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class LottoTest {
    @Test
    void 로또_번호의_개수가_6개가_넘어가면_예외가_발생한다() {
        assertThatThrownBy(() -> new Lotto(List.of(1, 2, 3, 4, 5, 6, 7)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 로또 번호는 6개여야 합니다.");
    }

    @DisplayName("로또 번호에 중복된 숫자가 있으면 예외가 발생한다.")
    @Test
    void 로또_번호에_중복된_숫자가_있으면_예외가_발생한다() {
        assertThatThrownBy(() -> new Lotto(List.of(1, 2, 3, 4, 5, 5)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 로또 번호는 중복되면 안됩니다.");
    }

    @DisplayName("로또 번호의 범위가 1~45가 아니면 예외가 발생한다.")
    @Test
    void lottoRangeException() {
        assertThatThrownBy(() -> new Lotto(List.of(1, 2, 3, 4, 5, 46)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 로또 번호는 1~45 사이의 숫자만 가집니다.");
    }

    @DisplayName("일치하는 번호의 수 반환")
    @Test
    void numberMatched() {
        Lotto a = new Lotto(List.of(1, 2, 3, 4, 5, 6));
        Lotto winningLotto = new Lotto(List.of(1, 2, 3, 4, 5, 7));
        int matchCount = a.matchNumbers(winningLotto);
        assertThat(matchCount).isEqualTo(5);
    }

    @DisplayName("보너스 번호와 일치 여부 반환")
    @Test
    void bonusNumberMatched() {
        Lotto myLotto = new Lotto(List.of(1, 2, 3, 4, 5, 6));
        Lotto winningLotto = new Lotto(List.of(4, 5, 6, 7, 8, 9));
        Bonus number = new Bonus(3, winningLotto);
        assertThat(myLotto.matchBonus(number)).isEqualTo(true);
    }

    @DisplayName("단일 로또 번호 반환")
    @Test
    void sortedNumbers() {
        Lotto myLotto = new Lotto(List.of(6, 5, 4, 3, 2, 1));
        Set<Integer> numbers = myLotto.getNumbers();
        assertThat(numbers).containsExactlyInAnyOrder(1, 2, 3, 4, 5, 6);
    }

}
