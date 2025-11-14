package precourse.openmission.bonusnumber;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import precourse.openmission.purchase.Purchase;
import precourse.openmission.purchase.PurchaseRepository;
import precourse.openmission.winninglotto.WinningLotto;
import precourse.openmission.winninglotto.WinningRepository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
public class BonusNumberServiceTest {
    @Autowired
    BonusNumberRepository bonusNumberRepository;

    @Autowired
    PurchaseRepository purchaseRepository;

    @Autowired
    BonusNumberService bonusNumberService;

    @Autowired
    WinningRepository winningRepository;

    Long purchaseId;
    Purchase savedPurchase;

    @BeforeEach
    void setUp() {
        bonusNumberRepository.deleteAll();
        winningRepository.deleteAll();
        purchaseRepository.deleteAll();

        Purchase purchase = new Purchase(2000, 2);
        WinningLotto newLotto = new WinningLotto("1,2,3,4,5,6", purchase);
        WinningLotto savedLotto = winningRepository.save(newLotto);

        savedPurchase = purchaseRepository.save(purchase);
        purchaseId = savedPurchase.getId();
    }

    @DisplayName("db에 구매 id에 따른 보너스 번호 저장 지휘")
    @Test
    void saveBonus() {
        //When
        BonusNumber savedBonus = bonusNumberService.saveBonus(8, purchaseId);

        //Then
        assertAll(
                () -> assertThat(savedBonus.getNumber()).isEqualTo(8),
                () -> assertThat(savedBonus.getId()).isEqualTo(purchaseId)
        );
    }

    @DisplayName("db에 구매 id에 따른 보너스 번호 조회 지휘")
    @Test
    void getBonus() {
        //Given
        BonusNumber savedBonus = bonusNumberService.saveBonus(8, purchaseId);

        //When
        BonusNumber foundBonus = bonusNumberService.getBonus(purchaseId);

        //Then
        assertAll(
                () -> assertThat(foundBonus.getNumber()).isEqualTo(8),
                () -> assertThat(foundBonus.getId()).isEqualTo(purchaseId)
        );
    }
}
