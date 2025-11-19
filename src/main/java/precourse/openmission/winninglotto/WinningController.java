package precourse.openmission.winninglotto;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 당첨 번호를 관리하는 API 컨트롤러 계층입니다.
 * 입력된 당첨 번호 저장 및 당첨 번호를 조회합니다.
 */
@RestController
@RequiredArgsConstructor
public class WinningController {
    private final WinningService winningService;

    /**
     * 구매 id를 통해 해당 회차의 당첨 번호를 입력받습니다.
     *
     * @param id                구매 id
     * @param winningRequestDTO 해당 구매 내역 회차의 당첨 번호
     * @return 상태코드 200. 유효성을 통과한 당첨 번호
     * @throws IllegalArgumentException 유효하지 않은 당첨번호와 id가 입력되었을 때, 상태코드 400을 반환합니다.
     */
    @PostMapping(value = "/issue/winninglotto/{id}")
    public WinningResponseDTO saveLotto(@PathVariable Long id, @RequestBody WinningRequestDTO winningRequestDTO) {
        WinningLotto newLotto = winningService.saveLotto(winningRequestDTO.getNumbers(), id);
        return lottoToDTO(newLotto);
    }

    private WinningResponseDTO lottoToDTO(WinningLotto winningLotto) {
        return new WinningResponseDTO(winningLotto);
    }

    /**
     * 등록된 당첨 번호를 조회합니다.
     *
     * @param id 조회할 구매 내역
     * @return 해당 회차의 구매 id와 번호가 포함된 DTO
     * @throws IllegalArgumentException 존재하지 않는 구매 id의 경우, 상태코드 400을 반환합니다.
     */
    @GetMapping(value = "/winninglotto/{id}")
    public WinningResponseDTO getLotto(@PathVariable Long id) {
        WinningLotto savedLotto = winningService.getLotto(id);
        return lottoToDTO(savedLotto);
    }

}
