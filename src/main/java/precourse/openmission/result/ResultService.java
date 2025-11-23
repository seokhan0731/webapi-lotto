package precourse.openmission.result;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import precourse.openmission.bonusnumber.BonusNumber;
import precourse.openmission.bonusnumber.BonusNumberRepository;
import precourse.openmission.pojo.*;
import precourse.openmission.mylotto.MyLotto;
import precourse.openmission.mylotto.MyLottoRepository;
import precourse.openmission.purchase.Purchase;
import precourse.openmission.purchase.PurchaseRepository;
import precourse.openmission.singlelottosuperentity.SingleLotto;
import precourse.openmission.winninglotto.WinningLotto;
import precourse.openmission.winninglotto.WinningRepository;

import java.util.*;

/**
 * 최종 로또 결과를 계산하는 서비스 계층입니다.
 */
@Service
@RequiredArgsConstructor
public class ResultService {
    private final PurchaseRepository purchaseRepository;
    private final MyLottoRepository myLottoRepository;
    private final WinningRepository winningRepository;
    private final BonusNumberRepository bonusRepository;

    /**
     * 수익률을 계산합니다.
     *
     * @param purchaseId 입력받은 구매 id
     * @return 계산된 수익률
     * @throws IllegalArgumentException 유효하지 않은 구매 id의 입력일 때, 발생합니다.
     * @throws IllegalStateException db에 발행 로또, 당첨 로또, 보너스 번호 중 어느 하나라도 존재하지 않는다면 발생합니다.
     */
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

    /**
     * Result 객체를 생성합니다.
     * 객체를 생성하기 전, db에 있는 발행 로또, 당첨 번호, 보너스 번호를 모두 pojo로 변환합니다.
     *
     * @param validId 검증된 id
     * @return 로또 결과를 관리하는 Result pojo
     * @throws IllegalStateException 발행 로또, 당첨 번호, 보너스 번호 중 하나라도 존재하지 않은 경우. 발생합니다.
     */
    private Result createResultPojo(Long validId) {
        List<Lotto> myLottoPojo = toMyLottoPojo(validId);
        Lotto winningLottoPojo = toWinningPojo(validId);
        Bonus bonusPojo = toBonusPojo(validId, winningLottoPojo);

        Lottos lottoCollection = new Lottos(myLottoPojo);

        return new Result(Rank.valueOf(lottoCollection.matchNumbersAll(winningLottoPojo),
                lottoCollection.matchBonusAll(bonusPojo)));
    }

    /**
     * 최종 순위 결과값을 계산합니다.
     * 상금: 당첨 횟수였던 ResultDTO를 등수: 당첨 횟수 형식으로 변환하여 반환합니다.
     *
     * @param purchaseId 입력받은 id
     * @return 등수: 당첨 횟수 형식 등수 결과값
     */
    public Map<String, Integer> getRankResult(Long purchaseId) {
        Purchase foundPurchase = validateId(purchaseId);

        Result lottoResult = createResultPojo(foundPurchase.getId());

        List<ResultDTO> sortedResultDTO = sortResultDTO(lottoResult);

        Map<String, Integer> rankMap = new LinkedHashMap<>();
        sortedResultDTO.forEach(resultDTO -> convertPrizeToRank(rankMap, resultDTO));

        return rankMap;
    }

    private List<ResultDTO> sortResultDTO(Result lottoResult) {
        return lottoResult.getResults()
                .stream()
                .sorted(Comparator.comparing(ResultDTO::prize).reversed())
                .toList();
    }

    private void convertPrizeToRank(Map<String, Integer> rankMap, ResultDTO singleResultDTO) {
        Rank matchedRank = Arrays.stream(Rank.values())
                .filter(r -> r.getPrize() == singleResultDTO.prize())
                .findFirst()
                .orElse(Rank.Other);

        rankMap.put(matchedRank.name(), singleResultDTO.number());
    }

}
