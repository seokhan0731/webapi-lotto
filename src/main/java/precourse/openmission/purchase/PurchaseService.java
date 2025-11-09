package precourse.openmission.purchase;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;


@Service
@RequiredArgsConstructor
public class PurchaseService {

    private final PurchaseRepository purchaseRepository;

    public void purchase(int money) {
        Amount amount = new Amount(money);

        int validAmount = amount.getAmount();
        int quantity = amount.buy();

        Purchase purchase = new Purchase(validAmount, quantity);
        purchaseRepository.save(purchase);
    }
}
