package precourse.openmission.winninglotto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import precourse.openmission.purchase.Purchase;
import precourse.openmission.purchase.PurchaseRepository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class WinningRepositoryTest {
    @Autowired
    WinningRepository winningRepository;

    @Autowired
    PurchaseRepository purchaseRepository;

    @DisplayName("당첨 로또 번호 저장 및 조회")
    @Test
    void storeWinningLotto() {
        //Given
        Purchase purchase = new Purchase(2000, 2);
        Purchase savedPurchase = purchaseRepository.save(purchase);
        Long purchaseId = savedPurchase.getId();
        String winningNumbers = "1,2,3,4,5,6";
        WinningLotto newLotto = new WinningLotto(winningNumbers, purchase);

        //When
        WinningLotto savedLotto = winningRepository.save(newLotto);

        //Given
        assertAll(
                () -> assertThat(savedLotto.getNumbers()).isEqualTo("1,2,3,4,5,6"),
                () -> assertThat(savedLotto.getId()).isEqualTo(purchaseId)
        );
    }
}
