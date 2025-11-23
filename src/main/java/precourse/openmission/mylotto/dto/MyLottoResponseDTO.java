package precourse.openmission.mylotto.dto;

import lombok.Getter;
import precourse.openmission.mylotto.MyLotto;

/**
 * 발행된 로또 출력시, 출력값을 정의하는 DTO
 * lottoId: 발행된 로또당 가지고 있는 고유 id
 * numbers: 발행된 로또당 가지고 있는 로또 번호(오름차순 정려뢴 상태)
 * purchaseID: 발행된 로또당 연결되어있는 구매 내역 id
 */
@Getter
public class MyLottoResponseDTO {
    private Long lottoId;
    private Long purchaseId;
    private String numbers;


    public MyLottoResponseDTO(MyLotto myLotto) {
        this.lottoId = myLotto.getId();
        this.numbers = myLotto.getNumbers();
        this.purchaseId = myLotto.getPurchase().getId();
    }
}
