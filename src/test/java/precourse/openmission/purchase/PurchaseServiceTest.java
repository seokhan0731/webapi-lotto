package precourse.openmission.purchase;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
public class PurchaseServiceTest {
    @Autowired
    PurchaseService purchaseService;

    @Autowired
    PurchaseRepository purchaseRepository;

    @DisplayName("db에 구매 금액 및 수량 저장 지휘")
    @Test
    void saveByService() {
        //Given
        int money = 2000;

        //When
        purchaseService.purchase(money);

        //Then
        List<Purchase> found = purchaseRepository.findAll();
        assertThat(found).hasSize(1);
        assertThat(found.get(0).getAmount()).isEqualTo(money);
        assertThat(found.get(0).getQuantity()).isEqualTo(2);
    }
}
