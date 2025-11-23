package precourse.openmission.winninglotto;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import precourse.openmission.pojo.Lotto;
import precourse.openmission.purchase.Purchase;
import precourse.openmission.purchase.PurchaseRepository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 당첨 번호 저장, 조회를 수행하는 서비스 계층입니다.
 */
@Service
@RequiredArgsConstructor
public class WinningService {
    private final WinningRepository winningRepository;
    private final PurchaseRepository purchaseRepository;

    /**
     * 입력된 당첨 번호를 저장합니다.
     * 도우미 메소드들을 통해, 구매 id 유효성 확인->로또 번호의 유효성 확인->문자열로 변환->저장순으로 수행됩니다.
     *
     * @param winningNumbers 입력받은 당첨 번호
     * @param purchaseId     입력받은 구매 id
     * @return 성공적으로 저장된 WinningLotto entity
     * @throws IllegalArgumentException 유효하지 않은 구매 id와 유효하지 않은 번호일때, 발생합니다.
     * @throws IllegalStateException 이미 당첨 로또가 존재한다면, 발생합니다.
     */
    @Transactional
    public WinningLotto saveLotto(List<Integer> winningNumbers, Long purchaseId) {
        Purchase foundPurchase = validateId(purchaseId);

        if (winningRepository.existsById(purchaseId)) {
            throw new IllegalStateException("[ERROR] 이미 당첨 로또가 발행되어 있습니다.");
        }
        Lotto winningLotto = new Lotto(winningNumbers);

        String numbersAsString = setToString(winningLotto);

        WinningLotto winningEntity = new WinningLotto(numbersAsString, foundPurchase);
        return winningRepository.save(winningEntity);
    }

    private Purchase validateId(Long purchaseId) {
        return purchaseRepository.findById(purchaseId).orElseThrow(() ->
                new IllegalArgumentException("[ERROR] 해당 구매 내역을 찾을 수 없습니다."));
    }

    private String setToString(Lotto winningLotto) {
        Set<Integer> set = winningLotto.getNumbers();

        return set.stream()
                .sorted()
                .map(String::valueOf)
                .collect(Collectors.joining(","));
    }

    public WinningLotto getLotto(Long purchaseId) {
        Purchase foundPurchase = validateId(purchaseId);
        return winningRepository.findByPurchaseId(foundPurchase.getId());
    }
}
