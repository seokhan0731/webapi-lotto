package precourse.openmission.issue;

import lombok.Getter;

@Getter
public class MyLottoResponseDTO {
    private Long lottoId;
    private String numbers;
    private Long purchaseId;

    public MyLottoResponseDTO(MyLotto myLotto) {
        this.lottoId = myLotto.getId();
        this.numbers = myLotto.getNumbers();
        this.purchaseId = myLotto.getPurchase().getId();
    }
}
