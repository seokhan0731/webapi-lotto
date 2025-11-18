package precourse.openmission.result;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import precourse.openmission.bonusnumber.BonusNumber;
import precourse.openmission.bonusnumber.BonusNumberRepository;
import precourse.openmission.domain.*;
import precourse.openmission.mylotto.MyLotto;
import precourse.openmission.mylotto.MyLottoRepository;
import precourse.openmission.purchase.Purchase;
import precourse.openmission.purchase.PurchaseRepository;
import precourse.openmission.singlelottosuperentity.SingleLotto;
import precourse.openmission.winninglotto.WinningLotto;
import precourse.openmission.winninglotto.WinningRepository;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ResultService {
    private final PurchaseRepository purchaseRepository;
    private final MyLottoRepository myLottoRepository;
    private final WinningRepository winningRepository;
    private final BonusNumberRepository bonusRepository;

    @Transactional
    public double getProfitRate(Long purchaseId) {
        Purchase foundPurchase = validateId(purchaseId);

        List<Lotto> myLottoPojo = toMyLottoPojo(purchaseId);
        Lotto winningLottoPojo = toWinningPojo(purchaseId);
        Bonus bonusPojo = toBonusPojo(purchaseId, winningLottoPojo);

        Lottos lottoCollection = new Lottos(myLottoPojo);

        Result lottoResult = new Result(Rank.valueOf(lottoCollection.matchNumbersAll(winningLottoPojo),
                lottoCollection.matchBonusAll(bonusPojo)));

        return lottoResult.profitRate();
    }

    private Purchase validateId(Long purchaseId) {
        return purchaseRepository.findById(purchaseId).orElseThrow(() ->
                new IllegalArgumentException("[ERROR] 해당 구매 내역을 찾을 수 없습니다."));
    }

    private List<Lotto> toMyLottoPojo(Long validId) {
        List<MyLotto> lottosEntity = myLottoRepository.findByPurchaseId(validId);
        return lottosEntity.stream()
                .map(SingleLotto::toLottoPojo)
                .toList();
    }

    private Lotto toWinningPojo(Long validId) {
        WinningLotto winningEntity = winningRepository.findByPurchaseId(validId);
        return winningEntity.toLottoPojo();
    }

    private Bonus toBonusPojo(Long validId, Lotto winnigLotto) {
        BonusNumber bonusEntity = bonusRepository.findByPurchaseId(validId);
        return new Bonus(bonusEntity.getNumber(), winnigLotto);
    }

}
