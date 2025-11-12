package precourse.openmission.bonusnumber;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import precourse.openmission.purchase.Purchase;
import precourse.openmission.purchase.PurchaseRepository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public class BonusNumberRepositoryTest {
    @Autowired
    BonusNumberRepository bonusNumberRepository;

    @Autowired
    PurchaseRepository purchaseRepository;

    @DisplayName("보너스 번호 저장")
    @Test
    void saveBonus() {
        //Given
        Purchase purchase = new Purchase(2000, 2);
        Purchase savedPurchase = purchaseRepository.save(purchase);
        Long purchaseId = savedPurchase.getId();
        String bonus = "1";
        BonusNumber bonusNumber = new BonusNumber(bonus, purchase);

        //When
        BonusNumber savedBonus = bonusNumberRepository.save(bonusNumber);

        //Given
        assertThat(savedBonus.getNumber()).isEqualTo("1");
        assertThat(savedBonus.getId()).isEqualTo(purchaseId);

    }
}
