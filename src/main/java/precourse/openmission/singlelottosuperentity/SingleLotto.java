package precourse.openmission.singlelottosuperentity;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import precourse.openmission.domain.Lotto;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
