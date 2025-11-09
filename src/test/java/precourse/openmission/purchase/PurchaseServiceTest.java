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

    @BeforeEach
    void setUp() {
        //Given
        int money = 2000;
        purchaseRepository.deleteAll();
        purchaseService.purchase(money);
    }

    @DisplayName("db에 구매 금액 및 수량 저장 지휘")
    @Test
    void saveByService() {
        //When
        List<Purchase> found = purchaseRepository.findAll();

        //Then
        assertThat(found).hasSize(1);
        assertThat(found.get(0).getAmount()).isEqualTo(2000);
        assertThat(found.get(0).getQuantity()).isEqualTo(2);
    }

    @DisplayName("db에 저장 된 값 조회")
    @Test
    void getByService() {
        //When
        List<Purchase> getByService = purchaseService.getPurchases();

        //Then
        assertThat(getByService).hasSize(1);
        assertThat(getByService.get(0).getAmount()).isEqualTo(2000);
        assertThat(getByService.get(0).getQuantity()).isEqualTo(2);
    }
}
