package precourse.openmission.purchase;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import precourse.openmission.domain.Amount;

import java.util.List;


@Service
@RequiredArgsConstructor
public class PurchaseService {

    private final PurchaseRepository purchaseRepository;

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
