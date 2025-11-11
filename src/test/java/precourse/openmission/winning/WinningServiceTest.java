package precourse.openmission.winning;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import precourse.openmission.purchase.Purchase;
import precourse.openmission.purchase.PurchaseRepository;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
public class WinningServiceTest {
    @Autowired
    WinningService winningService;

    @Autowired
    WinningRepository winningRepository;

    @Autowired
    PurchaseRepository purchaseRepository;

    Purchase savedPurchase;
    Long purchaseId;
    List<Integer> winningNumbers = List.of(1, 2, 3, 4, 5, 6);
    String winningNumbersAsString;

    @BeforeEach
    void setUp() {
        winningRepository.deleteAll();
        purchaseRepository.deleteAll();

        Purchase purchase = new Purchase(2000, 2);
        savedPurchase = purchaseRepository.save(purchase);
        purchaseId = savedPurchase.getId();
        winningNumbersAsString = winningNumbers.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));
    }

    @DisplayName("db에 구매 id에 따른 당첨 로또 번호 저장 지휘")
    @Test
    void saveWinningLotto() {
        //When
        WinningLotto savedLotto = winningService.saveLotto(winningNumbers, purchaseId);

        //Then
        assertAll(
                () -> assertThat(savedLotto.getNumbers()).isEqualTo(winningNumbersAsString),
                () -> assertThat(savedLotto.getId()).isEqualTo(purchaseId)
        );
    }

    @DisplayName("db에 구매 id에 따른 당첨 로또 번호 조회 지휘")
    @Test
    void viewWinningLotto() {
        //Given
        WinningLotto savedLotto = winningService.saveLotto(winningNumbers, purchaseId);

        //When
        WinningLotto foundLotto = winningService.getLotto(purchaseId);

        //Then
        assertAll(
                () -> assertThat(foundLotto.getNumbers()).isEqualTo(winningNumbersAsString),
                () -> assertThat(foundLotto.getId()).isEqualTo(purchaseId)
        );
    }

    @DisplayName("유효하지 않은 ID로 로또 번호 저장 및 조회")
    @Test
    void saveAndViewLottoByFakeId() {
        //Given
        Long fakeId = 123L;

        //Then
        assertAll(
                () -> assertThatThrownBy(() -> winningService.saveLotto(winningNumbers, fakeId))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("[ERROR] 해당 구매 내역을 찾을 수 없습니다."),
                () -> assertThatThrownBy(() -> winningService.getLotto(fakeId))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("[ERROR] 해당 구매 내역을 찾을 수 없습니다.")
        );
    }

    @DisplayName("6개가 아닌 로또 번호 저장 예외 구현")
    @Test
    void not6Numbers() {
        assertThatThrownBy(() -> winningService.saveLotto(List.of(1, 2, 3, 4, 5, 6, 7), purchaseId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 로또 번호는 6개여야 합니다.");
    }

    @DisplayName("유효한 로또 범위가 아닌 숫자 저장 예외 구현")
    @Test
    void invalidRangeNumbers() {
        assertThatThrownBy(() -> winningService.saveLotto(List.of(0, 2, 3, 4, 5, 6), purchaseId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 로또 번호는 1~45 사이의 숫자만 가집니다.");
    }


}
