package precourse.openmission.purchase.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 입력 받을 JSON 형식을 정의한 DTO
 * "money : 금액"형식의 입력의 형식에 해당합니다.
 */
@Getter
@Setter
public class PurchaseRequestDTO {
    private int money;
}
