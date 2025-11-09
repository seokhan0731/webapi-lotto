package precourse.openmission.domain;

import camp.nextstep.edu.missionutils.Randoms;

import java.util.List;

/**
 * 로또 랜덤 숫자 제공을 위한 클래스입니다.
 */
public class RandomGenerator implements NumberGenerator {

    @Override
    public List<Integer> generateNumbers() {
        return Randoms.pickUniqueNumbersInRange(1, 45, 6);
    }
}
