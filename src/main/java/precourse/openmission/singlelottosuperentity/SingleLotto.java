package precourse.openmission.singlelottosuperentity;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import precourse.openmission.pojo.Lotto;

import java.util.Arrays;
import java.util.List;

/**
 * 단일 로또로 변환 가능한 entity를 Lotto pojo로 변환해줍니다.
 * MyLotto, WinningLotto entity가 이에 해당합니다.
 */
@MappedSuperclass
@Getter
public abstract class SingleLotto {
    protected String numbers;

    public Lotto toLottoPojo() {
        String[] numbers = this.numbers.split(",");
        List<Integer> numberList = Arrays.stream(numbers)
                .map(Integer::parseInt)
                .toList();
        return new Lotto(numberList);
    }
}
