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

import java.util.*;

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
        Result lottoResult = createResultPojo(foundPurchase.getId());
        return lottoResult.profitRate();
    }

    private Purchase validateId(Long purchaseId) {
        return purchaseRepository.findById(purchaseId).orElseThrow(() ->
                new IllegalArgumentException("[ERROR] 해당 구매 내역을 찾을 수 없습니다."));
    }

    private List<Lotto> toMyLottoPojo(Long validId) {
        if (myLottoRepository.findByPurchaseId(validId).isEmpty()) {
            throw new IllegalStateException("[ERROR] 로또 발행이 되어 있지 않습니다.");
        }

        List<MyLotto> lottosEntity = myLottoRepository.findByPurchaseId(validId);
        return lottosEntity.stream()
                .map(SingleLotto::toLottoPojo)
                .toList();
    }

    private Lotto toWinningPojo(Long validId) {
        if (!(winningRepository.existsById(validId))) {
            throw new IllegalStateException("[ERROR] 당첨 로또 발행이 되어 있지 않습니다.");
        }
        WinningLotto winningEntity = winningRepository.findByPurchaseId(validId);
        return winningEntity.toLottoPojo();
    }

    private Bonus toBonusPojo(Long validId, Lotto winnigLotto) {
        if (!(bonusRepository.existsById(validId))) {
            throw new IllegalStateException("[ERROR] 보너스 번호가 입력되어 있지 않습니다.");
        }
        BonusNumber bonusEntity = bonusRepository.findByPurchaseId(validId);
        return new Bonus(bonusEntity.getNumber(), winnigLotto);
    }

    public Map<String, Integer> getRankResult(Long purchaseId) {
        Purchase foundPurchase = validateId(purchaseId);

        Result lottoResult = createResultPojo(foundPurchase.getId());

        List<ResultDTO> sortedResultDTO = sortResultDTO(lottoResult);

        Map<String, Integer> rankMap = new LinkedHashMap<>();
        sortedResultDTO.forEach(resultDTO -> convertPrizeToRank(rankMap, resultDTO));

        return rankMap;
    }

    private void convertPrizeToRank(Map<String, Integer> rankMap, ResultDTO singleResultDTO) {
        Rank matchedRank = Arrays.stream(Rank.values())
                .filter(r -> r.getPrize() == singleResultDTO.prize())
                .findFirst()
                .orElse(Rank.Other);

        rankMap.put(matchedRank.name(), singleResultDTO.number());
    }

    private Result createResultPojo(Long validId) {
        List<Lotto> myLottoPojo = toMyLottoPojo(validId);
        Lotto winningLottoPojo = toWinningPojo(validId);
        Bonus bonusPojo = toBonusPojo(validId, winningLottoPojo);

        Lottos lottoCollection = new Lottos(myLottoPojo);

        return new Result(Rank.valueOf(lottoCollection.matchNumbersAll(winningLottoPojo),
                lottoCollection.matchBonusAll(bonusPojo)));
    }

    private List<ResultDTO> sortResultDTO(Result lottoResult) {
        return lottoResult.getResults()
                .stream()
                .sorted(Comparator.comparing(ResultDTO::prize).reversed())
                .toList();
    }
}
