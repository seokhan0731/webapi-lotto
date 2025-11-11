package precourse.openmission.winninglotto;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class WinningController {
    private final WinningService winningService;

    @PostMapping(value = "/issue/winninglotto/{id}")
    public WinningResponseDTO saveLotto(@PathVariable Long id, @RequestBody WinningRequestDTO winningRequestDTO) {
        WinningLotto newLotto = winningService.saveLotto(winningRequestDTO.getNumbers(), id);
        return lottoToDTO(newLotto);
    }

    private WinningResponseDTO lottoToDTO(WinningLotto winningLotto) {
        return new WinningResponseDTO(winningLotto);
    }
}
