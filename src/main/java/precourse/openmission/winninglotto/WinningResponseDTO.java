package precourse.openmission.winninglotto;

import lombok.Getter;

/**
 * 당첨 로또 출력시, 출력값을 정의하는 DTO
 * numbers: 당첨 번호
 * purchaseId: 당첨 번호와 연결된 구매 내역의 고유 id
 */
@Getter
public class WinningResponseDTO {
    private Long purchaseId;
    private String numbers;

    public WinningResponseDTO(WinningLotto winningLotto) {
        this.numbers = winningLotto.getNumbers();
        this.purchaseId = winningLotto.getId();
    }
}
