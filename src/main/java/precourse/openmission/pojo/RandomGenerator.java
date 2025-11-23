package precourse.openmission.pojo;

import camp.nextstep.edu.missionutils.Randoms;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 로또 랜덤 숫자 제공을 위한 클래스입니다.
 */
@Component
public class RandomGenerator implements NumberGenerator {

    @Override
    public List<Integer> generateNumbers() {
        return Randoms.pickUniqueNumbersInRange(1, 45, 6);
    }
}
