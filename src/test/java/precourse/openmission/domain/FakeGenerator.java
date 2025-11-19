package precourse.openmission.domain;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class FakeGenerator implements NumberGenerator {
    private final Queue<Integer> numbers;
    private static final int AMOUNT_PER_LOTTO = 6;

    public FakeGenerator(List<Integer> numbers) {
        this.numbers = new LinkedList<>(numbers);
    }

    @Override
    public List<Integer> generateNumbers() {
        List<Integer> listPerLotto = new ArrayList<>();
        int number = 0;
        for (int i = 0; i < AMOUNT_PER_LOTTO; i++) {
            number = numbers.poll();
            listPerLotto.add(number);
        }
        return listPerLotto;
    }
}
