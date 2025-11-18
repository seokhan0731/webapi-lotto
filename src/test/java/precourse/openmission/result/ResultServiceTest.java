package precourse.openmission.result;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import precourse.openmission.bonusnumber.BonusNumber;
import precourse.openmission.bonusnumber.BonusNumberRepository;
import precourse.openmission.bonusnumber.BonusNumberService;
import precourse.openmission.mylotto.MyLotto;
import precourse.openmission.mylotto.MyLottoRepository;
import precourse.openmission.purchase.Purchase;
import precourse.openmission.purchase.PurchaseRepository;
import precourse.openmission.winninglotto.WinningLotto;
import precourse.openmission.winninglotto.WinningRepository;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class ResultServiceTest {
    @Autowired
    ResultService resultService;
    @Autowired
    PurchaseRepository purchaseRepository;
    @Autowired
    WinningRepository winningRepository;
    @Autowired
    BonusNumberRepository bonusRepository;

    @Autowired
    BonusNumberService bonusNumberService;
    @Autowired
    MyLottoRepository myLottoRepository;


    Long purchaseId;
    Purchase savedPurchase;

    @BeforeEach
    void setUp() {
        myLottoRepository.deleteAll();
        winningRepository.deleteAll();
        bonusRepository.deleteAll();
        purchaseRepository.deleteAll();

        Purchase purchase = new Purchase(8000, 8);

        WinningLotto winningLotto = new WinningLotto("1,2,3,4,5,6", purchase);
        WinningLotto savedLotto = winningRepository.save(winningLotto);


        savedPurchase = savedLotto.getPurchase();
        purchaseId = savedPurchase.getId();

        bonusNumberService.saveBonus(7, purchaseId);

        MyLotto lotto1 = new MyLotto("8,21,23,41,42,45", savedPurchase);
        MyLotto lotto2 = new MyLotto("3,5,11,16,32,38", savedPurchase);
        MyLotto lotto3 = new MyLotto("7,11,16,35,36,44", savedPurchase);
        MyLotto lotto4 = new MyLotto("1,8,11,31,41,42", savedPurchase);
        MyLotto lotto5 = new MyLotto("13,14,16,38,42,45", savedPurchase);
        MyLotto lotto6 = new MyLotto("7,11,30,40,42,43", savedPurchase);
        MyLotto lotto7 = new MyLotto("2,13,22,32,38,45", savedPurchase);
        MyLotto lotto8 = new MyLotto("1,3,5,14,22,45", savedPurchase);

        myLottoRepository.saveAll(List.of(lotto1, lotto2, lotto3, lotto4, lotto5, lotto6, lotto7, lotto8));

    }

    @DisplayName("수익률 계산")
    @Test
    void calculateProfitRate() {
        //WHen
        double profitRate = resultService.getProfitRate(purchaseId);

        //Then
        assertThat(profitRate).isEqualTo(62.5);
    }

}
