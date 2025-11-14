package precourse.openmission.bonusnumber;

import lombok.Getter;

@Getter
public class BonusResponseDTO {
    private Long purchaseId;
    private int number;

    public BonusResponseDTO(BonusNumber bonusNumber) {
        this.purchaseId = bonusNumber.getId();
        this.number = bonusNumber.getNumber();
    }
}
