package precourse.openmission.winninglotto;

import lombok.Getter;

@Getter
public class WinningResponseDTO {
    private String numbers;
    private Long purchaseId;

    public WinningResponseDTO(WinningLotto winningLotto) {
        this.numbers = winningLotto.getNumbers();
        this.purchaseId = winningLotto.getId();
    }
}
