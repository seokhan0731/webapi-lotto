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
        //구매 id 갖고오기
        Purchase foundPurchase = validateId(purchaseId);
        List<MyLotto> foundLottos = myLottoRepository.findByPurchaseId(purchaseId);
        WinningLotto foundWinning = winningRepository.findByPurchaseId(purchaseId);
        BonusNumber foundBonus = bonusRepository.findById(purchaseId).get();

        List<Lotto> myLottoPojo = foundLottos.stream()
                .map(myLottoEntity -> {
                    String[] numbers = myLottoEntity.getNumbers().split(",");

                    List<Integer> numberList = Arrays.stream(numbers)
                            .map(Integer::parseInt)
                            .toList();
                    return new Lotto(numberList);
                })
                .toList();
        String[] winningNumbers = foundWinning.getNumbers().split(",");
        List<Integer> winningList = Arrays.stream(winningNumbers)
                .map(Integer::parseInt)
                .toList();
        Lotto winningLottoPojo = new Lotto(winningList);

        Bonus bonusPojo = new Bonus(foundBonus.getNumber(), winningLottoPojo);

        Lottos lottoCollection = new Lottos(myLottoPojo);

        Result lottoResult = new Result(Rank.valueOf(lottoCollection.matchNumbersAll(winningLottoPojo),
                lottoCollection.matchBonusAll(bonusPojo)));

        return lottoResult.profitRate();
    }

    private Purchase validateId(Long purchaseId) {
        return purchaseRepository.findById(purchaseId).orElseThrow(() ->
                new IllegalArgumentException("[ERROR] 해당 구매 내역을 찾을 수 없습니다."));
    }
}
