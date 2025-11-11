package precourse.openmission.mylotto;

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
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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


    Purchase savedPurchase;
    Long purchaseId;

    @BeforeEach
    void setUp() {
        myLottoRepository.deleteAll();
        purchaseRepository.deleteAll();

        Purchase purchase = new Purchase(2000, 2);
        savedPurchase = purchaseRepository.save(purchase);
        purchaseId = savedPurchase.getId();
        Mockito.when(numberGenerator.generateNumbers())
                .thenReturn(List.of(1, 2, 3, 4, 5, 6))
                .thenReturn(List.of(7, 8, 9, 10, 11, 12));
    }

    @DisplayName("db에 해당 수량에 따른 로또 번호 저장 지휘")
    @Test
    void saveMyLottos() {
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

    @DisplayName("ID에 따른 발행된 로또 번호 조회")
    @Test
    void getMyLottos() {
        //When
        List<MyLotto> Lottos = myLottoService.saveLottos(purchaseId);
        List<MyLotto> returnLottos = myLottoService.getLottos(purchaseId);

        //Then
        assertAll(
                () -> assertThat(returnLottos).hasSize(2),
                () -> assertThat(returnLottos.get(0).getPurchase().getId()).isEqualTo(purchaseId),
                () -> assertThat(returnLottos.get(1).getPurchase().getId()).isEqualTo(purchaseId),
                () -> assertThat(returnLottos.get(0).getNumbers()).isEqualTo("1,2,3,4,5,6"),
                () -> assertThat(returnLottos.get(1).getNumbers()).isEqualTo("7,8,9,10,11,12")
        );
    }

    @DisplayName("유효하지 않은 ID로 로또 번호 저장 시도")
    @Test
    void saveLottosByFakeId() {
        //When
        Long fakeId = 123L;

        //Then
        assertThatThrownBy(() -> myLottoService.saveLottos(fakeId)).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 해당 구매 내역을 찾을 수 없습니다.");
    }

    @DisplayName("유효하지 않은 ID로 로또 번호 조회 시도")
    @Test
    void getLottosByFakeId() {
        //When
        Long fakeId = 123L;

        //Then
        assertThatThrownBy(() -> myLottoService.getLottos(fakeId)).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 해당 구매 내역을 찾을 수 없습니다.");
    }
}
