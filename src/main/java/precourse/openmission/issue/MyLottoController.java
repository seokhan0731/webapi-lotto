package precourse.openmission.issue;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class MyLottoController {
    private final MyLottoService myLottoService;

    @PostMapping(value = "/issue/{id}")
    public List<MyLottoResponseDTO> issue(@PathVariable Long id) {
        List<MyLotto> lottos = myLottoService.saveLottos(id);

        return myLottoToDTO(lottos);
    }

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

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleException(IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
}
