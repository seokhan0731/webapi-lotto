package precourse.openmission.winninglotto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 당첨 번호 입력시, 입력값을 정의하는 DTO
 */
@Getter
@Setter
public class WinningRequestDTO {
    private List<Integer> numbers;
}
