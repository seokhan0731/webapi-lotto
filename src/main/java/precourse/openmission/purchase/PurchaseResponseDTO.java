package precourse.openmission.purchase;

import lombok.Getter;

@Getter
public class PurchaseResponseDTO {
    private Long purchaseID;
    private int money;
    private int quantity;

    public PurchaseResponseDTO(Purchase myPurchase) {
        this.purchaseID = myPurchase.getId();
        this.money = myPurchase.getMoney();
        this.quantity = myPurchase.getQuantity();
    }
}
