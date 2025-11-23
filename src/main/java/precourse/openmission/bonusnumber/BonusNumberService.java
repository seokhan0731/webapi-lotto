package precourse.openmission.bonusnumber;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import precourse.openmission.pojo.Bonus;
import precourse.openmission.pojo.Lotto;
import precourse.openmission.purchase.Purchase;
import precourse.openmission.purchase.PurchaseRepository;
import precourse.openmission.winninglotto.WinningLotto;
import precourse.openmission.winninglotto.WinningRepository;

/**
 * 보너스 번호 저장과 조회를 수행하는 서비스 계층입니다.
 */
@Service
@RequiredArgsConstructor
public class BonusNumberService {
    private final BonusNumberRepository bonusNumberRepository;
    private final PurchaseRepository purchaseRepository;
    private final WinningRepository winningRepository;

    /**
     * 입력된 보너스 번호를 저장합니다.
     * 구매 id 유효성 검사->보너스 번호 유효성 확인->저장순으로 수행됩니다.
     * 보너스 번호의 유효성 확인을 위해, winningRepository에서 구매 id에 대응하는 당첨 번호를 가져옵니다.
     *
     * @param number     입력받은 보너스 번호
     * @param purchaseId 입력받은 구매 id
     * @return 저장에 성공한 BonusNumber 객체
     * @throws IllegalArgumentException 유효하지 않은 구매 id와 유효하지 않은 보너스 번호일 때, 발생합니다.
     * @throws IllegalStateException    당첨 번호가 저장되지 않은 상태에서 보너스 번호 저장을 시도하는 경우 발생합니다.
     *                                  이미 보너스 번호가 등록되어 있는 경우 발생합니다.
     */
    @Transactional
    public BonusNumber saveBonus(int number, Long purchaseId) {
        Purchase foundPurchase = validateId(purchaseId);
        if(bonusNumberRepository.existsById(purchaseId)){
            throw new IllegalStateException("[ERROR] 이미 보너스 번호가 등록되어 있습니다.");
        }
        if (!(winningRepository.existsById(purchaseId))) {
            throw new IllegalStateException("[ERROR] 당첨 번호 입력이 선행되어야 합니다.");
        }

        WinningLotto savedLotto = winningRepository.findByPurchaseId(purchaseId);
        Lotto winningLotto = savedLotto.toLottoPojo();

        Bonus newBonus = new Bonus(number, winningLotto);

        BonusNumber bonusEntity = new BonusNumber(newBonus.getNumber(), foundPurchase);
        return bonusNumberRepository.save(bonusEntity);
    }

    private Purchase validateId(Long purchaseId) {
        return purchaseRepository.findById(purchaseId).orElseThrow(() ->
                new IllegalArgumentException("[ERROR] 해당 구매 내역을 찾을 수 없습니다."));
    }

    public BonusNumber getBonus(Long purchaseId) {
        Purchase validPurchase = validateId(purchaseId);
        return bonusNumberRepository.findByPurchaseId(purchaseId);
    }
}
