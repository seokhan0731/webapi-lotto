package precourse.openmission.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class BonusTest {
    @DisplayName("보너스 번호의 범위가 1~45가 아니면 예외가 발생한다.")
    @Test
    void bonusRangeException() {
        Lotto winningLotto = new Lotto(List.of(1, 2, 3, 4, 5, 42));
        assertThatThrownBy(() -> new Bonus(46, winningLotto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 보너스 번호는 1~45 사이의 숫자만 가집니다.");
    }

    @DisplayName("보너스 번호가 로또 번호와 중복되면 예외가 발생한다.")
    @Test
    void bonusDuplicateException() {
        Lotto winningLotto = new Lotto(List.of(1, 2, 3, 4, 5, 42));
        assertThatThrownBy(() -> new Bonus(42, winningLotto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 보너스 번호는 로또 번호와 중복되면 안됩니다.");
    }

}
