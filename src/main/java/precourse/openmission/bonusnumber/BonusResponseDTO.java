package precourse.openmission.bonusnumber;

import lombok.Getter;

/**
 * 보너스 번호 반환시, 반환값을 정의하는 DTO
 */
@Getter
public class BonusResponseDTO {
    private Long purchaseId;
    private int number;

    public BonusResponseDTO(BonusNumber bonusNumber) {
        this.purchaseId = bonusNumber.getId();
        this.number = bonusNumber.getNumber();
    }
}
