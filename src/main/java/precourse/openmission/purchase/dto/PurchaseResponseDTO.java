package precourse.openmission.purchase.dto;

import lombok.Getter;
import precourse.openmission.purchase.Purchase;

/**
 * 출력해줄 JSON 형식을 정의한 DTO
 * "purchaseID : 아이디번호"형식에 해당합니다.
 * "money : 금액"형식의 입력의 형식에 해당합니다.
 * "quantity : 수량"형식에 해당합니다.
 */
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
