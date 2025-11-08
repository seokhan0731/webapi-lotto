package precourse.openmission.purchase;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class PurchaseRepositoryTest {
    @Autowired
    PurchaseRepository purchaseRepository;

    @DisplayName("구매 금액, 수량을 저장하고, 조회")
    @Test
    void amountSaveAndGet() {
        //Given
        Purchase purchase = new Purchase(5000, 5);

        //When
        Purchase savedPurchase = purchaseRepository.save(purchase);

        //Then
        Purchase found = purchaseRepository.findById(savedPurchase.getId()).orElse(null);
        assertThat(found).isNotNull();
        assertThat((found.getAmount())).isEqualTo(purchase.getAmount());
        assertThat((found.getQuantity())).isEqualTo(purchase.getQuantity());
    }
}
