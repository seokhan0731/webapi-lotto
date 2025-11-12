package precourse.openmission.bonusnumber;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import precourse.openmission.domain.Bonus;
import precourse.openmission.domain.Lotto;
import precourse.openmission.purchase.Purchase;
import precourse.openmission.purchase.PurchaseRepository;
import precourse.openmission.winninglotto.WinningLotto;
import precourse.openmission.winninglotto.WinningRepository;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BonusNumberService {
    private final BonusNumberRepository bonusNumberRepository;
    private final PurchaseRepository purchaseRepository;
    private final WinningRepository winningRepository;

    @Transactional
    public BonusNumber saveBonus(int number, Long purchaseId) {
        Purchase foundPurchase = validateId(purchaseId);

        WinningLotto savedLotto = winningRepository.findByPurchaseId(foundPurchase.getId());

        String Numbers = savedLotto.getNumbers();
        List<String> stringArray = Arrays.asList(Numbers.split(","));
        List<Integer> integerList = stringArray.stream()
                .mapToInt(Integer::parseInt)
                .boxed()
                .toList();

        Lotto winningLotto = new Lotto(integerList);
        Bonus newBonus = new Bonus(number, winningLotto);

        BonusNumber bonusEntity = new BonusNumber(newBonus.getNumber(), foundPurchase);
        return bonusNumberRepository.save(bonusEntity);
    }

    private Purchase validateId(Long purchaseId) {
        return purchaseRepository.findById(purchaseId).orElseThrow(() ->
                new IllegalArgumentException("[ERROR] 해당 구매 내역을 찾을 수 없습니다."));
    }
}
