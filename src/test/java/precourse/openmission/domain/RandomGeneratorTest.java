package precourse.openmission.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class RandomGeneratorTest {
    @DisplayName("랜덤한 숫자 6개 뽑기")
    @Test
    void generateNumbersTest() {
        NumberGenerator a = new RandomGenerator();
        assertThat(a.generateNumbers()).hasSize(6);
    }
}
