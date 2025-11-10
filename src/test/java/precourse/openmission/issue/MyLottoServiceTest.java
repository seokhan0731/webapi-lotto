package precourse.openmission.issue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import precourse.openmission.domain.NumberGenerator;
import precourse.openmission.purchase.Purchase;
import precourse.openmission.purchase.PurchaseRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
public class MyLottoServiceTest {
    @Autowired
    MyLottoService myLottoService;

    @Autowired
    MyLottoRepository myLottoRepository;

    @Autowired
    PurchaseRepository purchaseRepository;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public NumberGenerator numberGenerator() {
            return Mockito.mock(NumberGenerator.class);
        }
    }

    @Autowired
    NumberGenerator numberGenerator;

    @BeforeEach
    void setUp() {
        myLottoRepository.deleteAll();
        purchaseRepository.deleteAll();
    }

    @DisplayName("db에 해당 수량에 따른 로또 번호 저장 지휘")
    @Test
    void saveMyLottos() {
        //Given
        Purchase purchase = new Purchase(2000, 2);
        Purchase savedPurchase = purchaseRepository.save(purchase);
        Long purchaseId = savedPurchase.getId();
        Mockito.when(numberGenerator.generateNumbers())
                .thenReturn(List.of(1, 2, 3, 4, 5, 6))
                .thenReturn(List.of(7, 8, 9, 10, 11, 12));

        //When
        List<MyLotto> returnLottos = myLottoService.saveLottos(purchaseId);

        //Then
        assertAll(
                () -> assertThat(returnLottos).hasSize(2),
                () -> assertThat(returnLottos.get(0).getPurchase().getId()).isEqualTo(purchaseId),
                () -> assertThat(returnLottos.get(1).getPurchase().getId()).isEqualTo(purchaseId),
                () -> assertThat(returnLottos.get(0).getNumbers()).isEqualTo("1,2,3,4,5,6"),
                () -> assertThat(returnLottos.get(1).getNumbers()).isEqualTo("7,8,9,10,11,12")
        );
    }

}
