package precourse.openmission.mylotto;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import precourse.openmission.mylotto.dto.MyLottoResponseDTO;

import java.util.List;

/**
 * 발행된 로또를 관리하는 API 컨트롤러 계층입니다.
 * 로또 발행 및 발행된 로또 조회를 책임집니다.
 */
@RestController
@RequiredArgsConstructor
public class MyLottoController {
    private final MyLottoService myLottoService;

    /**
     * 구매 id를 받아 로또를 발행합니다.
     * 발행된 로또는 JSON 형식으로 출력됩니다.
     *
     * @param id 구매 id
     * @return HTTP StatusCode 200. 발행된 로또들의 리스트
     * @throws IllegalArgumentException 유효하지 않은 id가 입력되었을 때, HTTP 400을 반환합니다.
     */
    @PostMapping(value = "/issue/mylotto/{id}")
    public List<MyLottoResponseDTO> issue(@PathVariable Long id) {
        List<MyLotto> lottos = myLottoService.saveLottos(id);

        return myLottoToDTO(lottos);
    }

    /**
     * 발행된 로또 번호들을 조회합니다.
     *
     * @param id 조회할 구매 내역
     * @return 해당 회차의 구매 id와 발행된 번호들이 포함된 DTO
     * @throws IllegalArgumentException 존재하지 않는 구매 id의 경우, 상태코드 400을 반환합니다.
     */
    @GetMapping(value = "/mylotto/{id}")
    public List<MyLottoResponseDTO> getMyLotto(@PathVariable Long id) {
        List<MyLotto> lottos = myLottoService.getLottos(id);

        return myLottoToDTO(lottos);
    }

    private List<MyLottoResponseDTO> myLottoToDTO(List<MyLotto> myLottos) {
        return myLottos.stream()
                .map(MyLottoResponseDTO::new)
                .toList();
    }

}
