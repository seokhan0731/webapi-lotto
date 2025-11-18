package precourse.openmission.mylotto;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import precourse.openmission.domain.Lottos;
import precourse.openmission.domain.NumberGenerator;
import precourse.openmission.purchase.Purchase;
import precourse.openmission.purchase.PurchaseRepository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 로또 발행, 저장, 조회에 해당하는 메인 로직을 수행하는 서비스 계층입니다.
 */
@Service
@RequiredArgsConstructor
public class MyLottoService {

    private final MyLottoRepository lottoRepository;
    private final PurchaseRepository purchaseRepository;
    private final NumberGenerator numberGenerator;

    /**
     * 로또의 발행부터 저장까지 전체 흐름을 지휘합니다.
     * 도우미 메소드들을 통해, 구매 id 유효성 확인->로또 발행->로또 번호 문자열로 변환->저장순으로 수행됩니다
     *
     * @param purchaseId 유효성 검증이 필요한 구매 id
     * @return 저장된 MyLotto Entity의 리스트
     * @throws IllegalArgumentException 유효하지 않은 구매 id
     * @throws IllegalStateException 이미 로또가 발행되어 있다면 발생합니다.
     */
    public List<MyLotto> saveLottos(Long purchaseId) {
        Purchase foundPurchase = validateId(purchaseId);
        if (lottoRepository.existsByPurchaseId(purchaseId)) {
            throw new IllegalStateException("[ERROR] 이미 로또가 발행되어 있습니다.");
        }
        List<MyLotto> lottosToSave = createListOfLottoEntities(foundPurchase);
        return lottoRepository.saveAll(lottosToSave);
    }

    /**
     * 유효하지 않은 구매 id를 걸러냅니다.
     * 유효하다면 해당 구매 id를 갖는 Purchase를 반환합니다.
     *
     * @param purchaseId 유효성 검증이 필요한 구매 id
     * @return 유효한 구매 id를 갖는 Purchase 객체
     */
    private Purchase validateId(Long purchaseId) {
        return purchaseRepository.findById(purchaseId).orElseThrow(() ->
                new IllegalArgumentException("[ERROR] 해당 구매 내역을 찾을 수 없습니다."));
    }

    private List<Set<Integer>> issueLottos(Purchase foundPurchase) {
        Lottos myLottos = new Lottos(foundPurchase.getQuantity());
        myLottos.issueLottos(numberGenerator);
        return myLottos.getNumbersAll();
    }

    private String setToString(Set<Integer> set) {
        return set.stream()
                .sorted()
                .map(String::valueOf)
                .collect(Collectors.joining(","));
    }

    /**
     * MyLotto들을 생성하고, 생성된 객체들을 리스트로 묶어 반환합니다.
     * 로또 발행->MyLotto entity 생성->리스트순으로 진행됩니다.
     *
     * @param foundPurchase 유효한 구매 id로 entity 생성시, 구매id와 묶기 위해 필요합니다.
     * @return 생성된 MyLotto 객체들의 리스트
     */
    private List<MyLotto> createListOfLottoEntities(Purchase foundPurchase) {
        List<Set<Integer>> allNumbersSet = issueLottos(foundPurchase);
        return allNumbersSet.stream()
                .map(singleSet -> {
                    String numbersAsString = setToString(singleSet);
                    return new MyLotto(numbersAsString, foundPurchase);
                })
                .toList();
    }

    public List<MyLotto> getLottos(Long purchaseId) {
        Purchase foundPurchase = validateId(purchaseId);
        return lottoRepository.findByPurchaseId(purchaseId);
    }
}
