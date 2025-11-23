package precourse.openmission.bonusnumber;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import precourse.openmission.bonusnumber.dto.BonusRequestDTO;
import precourse.openmission.bonusnumber.dto.BonusResponseDTO;

/**
 * 보너스 번호 저장과 조회를 책임지는 api 컨트롤러 계층입니다.
 */
@RestController
@RequiredArgsConstructor
public class BonusNumberController {
    private final BonusNumberService bonusNumberService;

    /**
     * 구매 id를 통해 해당 회차의 보너스 번호를 입력받습니다.
     *
     * @param id              구매 id
     * @param bonusRequestDTO 해당 구매 내역 회차의 보너스 번호
     * @return 상태코드 200, 유효성 검사를 통과한 보너스 번호
     * @throws IllegalArgumentException 유효하지 않은 보너스 번호와 id가 입력되었을 때, 발생합니다.
     * @throws IllegalStateException    당첨 번호가 저장되지 않은 상태에서 보너스 번호 저장 요청시, 발생합니다.
     */
    @PostMapping("/issue/bonus/{id}")
    public BonusResponseDTO saveBonus(@PathVariable Long id, @RequestBody BonusRequestDTO bonusRequestDTO) {
        BonusNumber newBonus = bonusNumberService.saveBonus(bonusRequestDTO.getNumber(), id);
        return bonusToDTO(newBonus);
    }

    private BonusResponseDTO bonusToDTO(BonusNumber bonusNumber) {
        return new BonusResponseDTO(bonusNumber);
    }

    /**
     * 저장된 보너스 번호를 조회합니다.
     *
     * @param id 조회할 구매 내역
     * @return 해당 회차의 구매 id와 보너스 번호가 포함된 DTO
     * @throws IllegalArgumentException 존재하지 않는 구매 id
     */
    @GetMapping("/bonus/{id}")
    public BonusResponseDTO getBonus(@PathVariable Long id) {
        BonusNumber savedBonus = bonusNumberService.getBonus(id);
        return bonusToDTO(savedBonus);
    }

}
