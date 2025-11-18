package precourse.openmission.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 여러 장의 로또를 관리하는 일급컬렉션입니다.
 */
public class Lottos {
    private List<Lotto> lottos;
    private final int quantity;

    /**
     * Lottos 객체를 생성합니다.
     * 단일 로또들은 이때 생성이 되지 않은 상태로, 단일 로또 생성은 issueLottos에서 진행됩니다.
     *
     * @param quantity 발행할 전체 로또 개수
     */
    public Lottos(int quantity) {
        this.lottos = new ArrayList<>(quantity);
        this.quantity = quantity;
    }

    /**
     * 최종 결과를 계산하기 위해 추가된 메소드입니다.
     * db에서 로또들을 불러와서, 해당 클래스에서 총괄 관리하게 합니다.
     *
     * @param lottos db에서 불러온 이미 발행된 상태의 로또들
     */
    public Lottos(List<Lotto> lottos) {
        this.lottos = lottos;
        this.quantity = lottos.size();
    }

    /**
     * 각 로또들을 생성합니다.
     * 유효한 로또 번호를 생성하는 생성기를 통해, 각 로또들을 생성합니다.
     *
     * @param generator 로또 번호 생성기
     * @throws IllegalArgumentException 각 로또를 생성할 때, 값의 유효성에 어긋나는 경우 발생합니다.
     */
    public void issueLottos(NumberGenerator generator) {
        while (lottos.size() < quantity) {
            List<Integer> numbers = generator.generateNumbers();
            Lotto newLotto = new Lotto(numbers);

            lottos.add(newLotto);
        }
    }

    public List<Lotto> getLottos() {
        return lottos;
    }

    public List<Integer> matchNumbersAll(Lotto winningLotto) {
        return lottos.stream()
                .map(lotto -> lotto.matchNumbers(winningLotto))
                .collect(Collectors.toList());
    }

    public List<Boolean> matchBonusAll(Bonus number) {
        return lottos.stream()
                .map(lotto -> lotto.matchBonus(number))
                .collect(Collectors.toList());
    }

    public List<Set<Integer>> getNumbersAll() {
        return lottos.stream()
                .map(Lotto::getNumbers)
                .collect(Collectors.toList());
    }
}
