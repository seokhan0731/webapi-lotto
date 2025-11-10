package precourse.openmission.issue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import precourse.openmission.purchase.Purchase;
import precourse.openmission.purchase.PurchaseRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;

@DataJpaTest
public class MyLottoRepositoryTest {
    @Autowired
    private PurchaseRepository purchaseRepository;

    @Autowired
    private MyLottoRepository myLottoRepository;

    @DisplayName("로또 번호 저장 및 조회")
    @Test
    void storeLotto() {
        //Given
        Purchase purchase = new Purchase(2000, 2);
        Purchase savedPurchase = purchaseRepository.save(purchase);
        Long purchaseId = savedPurchase.getId();
        List<String> lottoNumbers = List.of("1,2,3,4,5,6",
                "7,8,9,10,11,12"
        );
        List<MyLotto> newLottos = lottoNumbers.stream()
                .map(numbers -> new MyLotto(numbers, savedPurchase))
                .toList();

        //When
        List<MyLotto> savedLottos = myLottoRepository.saveAll(newLottos);

        //Then
        MyLotto firstLotto = savedLottos.get(0);
        MyLotto secondLotto = savedLottos.get(1);
        assertAll(
                () -> assertThat(savedLottos).hasSize(2),
                () -> assertThat(firstLotto.getPurchase().getId()).isEqualTo(savedPurchase.getId()),
                () -> assertThat(secondLotto.getPurchase().getId()).isEqualTo(savedPurchase.getId()),
                () -> assertThat(firstLotto.getNumbers()).isEqualTo("1,2,3,4,5,6"),
                () -> assertThat(secondLotto.getNumbers()).isEqualTo("7,8,9,10,11,12")
        );

    }

}
