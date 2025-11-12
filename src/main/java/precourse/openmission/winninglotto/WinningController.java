package precourse.openmission.winninglotto;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping(value = "/winninglotto/{id}")
    public WinningResponseDTO getLotto(@PathVariable Long id) {
        WinningLotto savedLotto = winningService.getLotto(id);
        return lottoToDTO(savedLotto);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleException(IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
}
