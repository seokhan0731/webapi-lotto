package precourse.openmission.winning;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import precourse.openmission.domain.Lotto;
import precourse.openmission.purchase.Purchase;
import precourse.openmission.purchase.PurchaseRepository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WinningService {
    private final WinningRepository winningRepository;
    private final PurchaseRepository purchaseRepository;

    @Transactional
    public WinningLotto saveLotto(List<Integer> winningNumbers, Long purchaseId) {
        Purchase foundPurchase = validateId(purchaseId);

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
}
