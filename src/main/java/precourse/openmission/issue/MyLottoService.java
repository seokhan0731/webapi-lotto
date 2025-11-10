package precourse.openmission.issue;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import precourse.openmission.domain.Lottos;
import precourse.openmission.domain.NumberGenerator;
import precourse.openmission.purchase.Purchase;
import precourse.openmission.purchase.PurchaseRepository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MyLottoService {

    private final MyLottoRepository lottoRepository;
    private final PurchaseRepository purchaseRepository;
    private final NumberGenerator numberGenerator;

    public List<MyLotto> saveLottos(Long purchaseId) {
        Purchase foundPurchase = validateId(purchaseId);
        List<MyLotto> lottosToSave = createListOfLottoEntities(foundPurchase);
        return lottoRepository.saveAll(lottosToSave);
    }

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
