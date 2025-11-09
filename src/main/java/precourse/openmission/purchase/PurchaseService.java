package precourse.openmission.purchase;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import precourse.openmission.domain.Amount;

import java.util.List;

/**
 * 리포지토리 계층을 통해 메인 로직을 수행하는 서비스 계층입니다.
 */
@Service
@RequiredArgsConstructor
public class PurchaseService {

    private final PurchaseRepository purchaseRepository;

    /**
     * 입력을 받은 값을 DB에 저장하고, 생성된 Purchase 객체를 반환합니다.
     *
     * @param money JSON 형식으로 입력받은 구매 금액
     * @return 구매 금액을 통해 생성된 Purchase 객체
     * @throws IllegalArgumentException money가 1000원 단위가 아니거나 양수가 아닌 경우 발생합니다.
     */
    public Purchase purchase(int money) {
        Amount amount = new Amount(money);

        int validAmount = amount.getAmount();
        int quantity = amount.buy();

        Purchase purchase = new Purchase(validAmount, quantity);
        purchaseRepository.save(purchase);

        return purchase;
    }

    public List<Purchase> getPurchases() {
        return purchaseRepository.findAll();
    }
}
