package precourse.openmission.issue;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
