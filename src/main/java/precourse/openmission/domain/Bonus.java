package precourse.openmission.domain;

import java.util.List;

/**
 * 보너스 번호 관련 로직을 수행하는 클래스입니다.
 * 보너스 번호의 유효성 검사와, 발행된 로또 번호와의 일치 여부를 판단합니다.
 */
public class Bonus {
    private final int number;

    /**
     * Bonus 객체를 생성합니다.
     *
     * @param number       입력된 보너스 번호
     * @param winningLotto 당첨 로또 번호 6개
     * @throws IllegalArgumentException 보너스 번호의 범위 오류, 로또 번호와의 중복이 존재하는 경우 발생합니다.
     */
    public Bonus(int number, Lotto winningLotto) {
        validate(number, winningLotto);
        this.number = number;
    }

    private void validate(int number, Lotto winningLotto) {
        if (!(number >= 1 && number <= 45)) {
            throw new IllegalArgumentException("[ERROR] 보너스 번호는 1~45 사이의 숫자만 가집니다.");
        }
        if (winningLotto.duplicateWithBonus(number)) {
            throw new IllegalArgumentException("[ERROR] 보너스 번호는 로또 번호와 중복되면 안됩니다.");
        }
    }

    /**
     * 발행된 1장의 로또 번호와의 일치 여부를 판단합니다.
     * Lotto 클래스의 보너스 번호 관련 도우미 함수 역할을 수행합니다.
     *
     * @param lottoNumbers 발행된 로또 번호
     * @return 보너스 번호와의 일치 여부
     */
    public boolean matchBonusNumber(List<Integer> lottoNumbers) {
        if (lottoNumbers.contains(number)) {
            return true;
        }
        return false;
    }
}
