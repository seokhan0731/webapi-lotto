package precourse.openmission.domain;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 단일 로또 관련 모든 로직을 수행하는 클래스입니다.
 */
public class Lotto {
    private final List<Integer> numbers;

    /**
     * Lotto 객체를 생성합니다.
     *
     * @param numbers 로또 번호 6개
     * @throws IllegalArgumentException 잘못된 번호 개수, 중복된 번호 존재, 잘못된 범위의 번호 존재의 경우 발생합니다.
     */
    public Lotto(List<Integer> numbers) {
        validate(numbers);
        this.numbers = numbers;
    }

    private void validate(List<Integer> numbers) {
        if (numbers.size() != 6) {
            throw new IllegalArgumentException("[ERROR] 로또 번호는 6개여야 합니다.");
        }
        if (numbers
                .stream()
                .distinct()
                .count() != numbers.size()) {
            throw new IllegalArgumentException("[ERROR] 로또 번호는 중복되면 안됩니다.");
        }
        if (!(numbers
                .stream()
                .allMatch(number -> number >= 1 && number <= 45))) {
            throw new IllegalArgumentException("[ERROR] 로또 번호는 1~45 사이의 숫자만 가집니다.");
        }
    }

    /**
     * 보너스 번호가 당첨 로또 번호와 중복되는지를 판별합니다.
     * Bonus에서의 유효성 검사시, 도우미 함수 역할을 수행합니다.
     *
     * @param bonusNUmber 보너스 번호
     * @return 보너스 번호와 로또 번호간 중복 여부
     */
    public boolean duplicateWithBonus(int bonusNUmber) {
        if (numbers.contains(bonusNUmber)) {
            return true;
        }
        return false;
    }

    /**
     * 당첨 로또와의 일치하는 번호 개수를 반환합니다.
     *
     * @param winningLotto 당첨 로또
     * @return 일치하는 번호의 개수
     */
    public int matchNumbers(Lotto winningLotto) {
        long count = numbers.stream()
                .filter(winningLotto.numbers::contains)
                .count();
        return (int) count;
    }

    /**
     * 보너스 번호와의 일치 여부를 반환합니다.
     *
     * @param bonusNumber 보너스 번호
     * @return 보너스 번호와의 일치 여부
     */
    public boolean matchBonus(Bonus bonusNumber) {
        return bonusNumber.matchBonusNumber(this.numbers);
    }

    public Set<Integer> getNumbers() {
        return new HashSet<>(numbers);
    }
}

