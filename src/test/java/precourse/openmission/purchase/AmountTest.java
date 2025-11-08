package precourse.openmission.purchase;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class AmountTest {
    @DisplayName("구입 금액 1,000원으로 나누어 떨어지지 않는 경우 예외처리")
    @Test
    void amountDivideException() {
        assertThatThrownBy(() -> new Amount(1500))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 구입 금액은 1,000원으로 나누어 떨어져야합니다.");
    }

    @DisplayName("구입 금액 양수가 아닌 경우 예외처리")
    @Test
    void notPositiveAmount() {
        assertThatThrownBy(() -> new Amount(-1000))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 구입 금액은 양수여야합니다.");
    }

    @DisplayName("구입 금액에 맞춘 발행할 로또 수 반환")
    @Test
    void howManyBuy() {
        Amount a = new Amount(10000);
        assertThat(a.buy()).isEqualTo(10);
    }

}
